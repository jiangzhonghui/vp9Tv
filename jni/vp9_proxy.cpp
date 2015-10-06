#include "ev.h"
#include <netinet/in.h>
#include <netdb.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <stdio.h>
#include <wchar.h>
#include <stdlib.h>
#include <fcntl.h>
#include <string.h>
#include <unistd.h>
#include <sys/types.h>
#include <pthread.h>
#include <errno.h>
#include <sys/stat.h>
#include <sys/time.h>
#include <time.h>
#include <vector>
#include <stdarg.h>
#include <iostream>

#define _ANDROID_

#ifdef _ANDROID_

#include <jni.h>
/*for android logs*/
#include <android/log.h>

#define LOG_TAG "VP9-proxy"

#ifndef LOG_VIA_SOCKET
#define LOGI(...) __android_log_print(4, LOG_TAG, __VA_ARGS__);
#endif
#define LOGE(...) __android_log_print(6, LOG_TAG, __VA_ARGS__);
#else
#define LOGI(...) printf(__VA_ARGS__);printf("\n")
#define LOGE(...) printf(__VA_ARGS__);printf("\n")
#endif

#define USAGE_STR "Usage: video_proxy ip port"
#define MAX_CONNECTION 10 //max of socket connection
#define NUM_OF_TS_FILE_PER_LIST 5 //number of ts files in m3u8 file
#define MAX_OF_TS_FILE_IN_MEMORY 15 //number of ts files saved in memory
#define MAX_CONTENT_SIZE 5000000 //size of vn and m3u8
#define BUFFER_SIZE 1000
#define MAX_OF_TS_LINK 30
#define PROXY_VERSION "08072015 19:28" // last update

#define TIME_IDLE 90

#define M3U8_RESPONCE_TIMEOUT 10

#define MAX_NUMBER_NOCHANGE_PLAYLIST 15

enum PROCESS_STATUS
{
	None = 0,
	Get_m3u8,
	Get_ts,
	Convert_ts,
};

enum TS_STATUS
{
	Waiting = 0, //waiting to download
	Downloaded, //
	Downloading, //
	Fail, //download fail
};

#define SUCCESS 0
#define NOT_CHANGE 1
#define NOT_FOUND 2
#define NOT_CONNECT 4
#define NOT_SEND 8
#define SLOW_SPEED 16
#define STOPED 32
#define NOT_CREATE 64
#define NOT_RESOLVE 128
#define NOT_CONVERT 256

#define STATE_NONE 0
#define STATE_GET_M3U8 1
#define STATE_GET_TS 2
#define STATE_CONVERT_TS 3
#define STATE_STOPPING 4

#define CONVERT_TIMEOUT 5.0
#define M3U8_DOWNLOAD_TIMEOUT 4.0
#define VN_DOWNLOAD_TIMEOUT 25.0

using namespace std;

extern "C"
{
	extern int ffmpeg_function(int argc, char **argv);
}

bool end_with(string url, string ext) 
{	
	std::size_t found = url.find(ext);
	if (found + ext.length() == url.length())
	{
		return true;
	}
	return false;
}

void get_ts_link(char* m3u8Content, string* ts_link, float* ts_time, int &number)
{
    number = 0;
    char *p = m3u8Content;
    char pattern[] = "#EXTINF";

    while (strstr(p, pattern) != NULL) 
    {
        char *begin_of_line = strstr(p, pattern);
        char *end_of_line = strstr(begin_of_line + strlen(pattern), "\n");
        if (end_of_line != NULL) 
        {
            char time_str[10];
            int time_str_len = (end_of_line - begin_of_line - strlen(pattern) - 1);
            if (time_str_len > 5)
			{
            	time_str_len = 5;
            }

            memcpy(time_str, begin_of_line + strlen(pattern) + 1, time_str_len);
            time_str[time_str_len] = 0;

            char *end_of_filename = strstr(end_of_line+1, "\n");
            if (end_of_filename != NULL) 
            {
				ts_link[number] = "";
				int len_of_file_name = end_of_filename-end_of_line - 1;
				for (int i=0; i< len_of_file_name; i++)
				{
					ts_link[number] += end_of_line[1+i];
				}
		
				if (end_with(ts_link[number], ".mp4")) 
				{
					ts_link[number][len_of_file_name-1] = 0;
					ts_link[number][len_of_file_name-2] = 's';
					ts_link[number][len_of_file_name-3] = 't';
					ts_link[number][len_of_file_name-4] = '.';
				}
                ts_time[number] = atof(time_str);
                if (ts_time[number] > 10.0)
                {
                	ts_time[number] = 10.0;
                }
                number++;
            }
            else 
            {
                break;
            }
            p = end_of_line;
        }
        else break;
    }
}

bool get_link(char *httpRequest, string &url, string &cookie)
{
    char *first = strstr(httpRequest, "GET");
    if (first == NULL)
    {
        return false;
    }
    first += 4;
    while (*first == '/') first++;

    char *last = strstr(httpRequest, "HTTP");
    if (last == NULL)
    {
        return false;
    }
    last -= 1;

    while (*last == ' ') last--;
	url = "";
	for (char* i=first; i<=last; i++)
	{
		url += char(first[i-first]);
	}

	cookie = "";
	first = strstr(httpRequest, "Cookie: ");
	if (first == NULL)
	{
		return true;
	}
	first += 8;

	last = strstr(first, "\r\n");
	if (last == NULL)
	{
		return false;
	}
	last -= 1;

	for (char* i=first; i<=last; i++)
	{
		cookie += char(first[i-first]);
	}
    
    return true;
}

bool parse_link(string fullUrl, string &host, string &url)
{
	std::size_t found = fullUrl.find("http://");
	
	if (found == std::string::npos) 
	{
		return false;
	}
	fullUrl = fullUrl.substr(7);

	found = fullUrl.find_first_of('/');
	if (found == std::string::npos)
	{
		return false;
	}
	
	host = fullUrl.substr(0, found);
	url = fullUrl.substr(found);
	return true;
}

class ts_file {
	public:
		string name;
		float duration;
		double downloadTime;
		int contentLength; //current content length
		int downloadedBytes; // downloaded Bytes
		char *contentBuff; //content buffer
		TS_STATUS status; //status of ts_file, is {downloading, downloaded, fail, prepareDownload}
		
		ts_file() 
		{
			name = "";
			duration = 0.0;
			downloadTime = 0.0;
			downloadedBytes = 0;
			contentLength = 0;
			contentBuff = NULL;
			status = Waiting;
		}
		
		ts_file(string _name, float _dur)
		{
			this->status = Waiting;
			this->name = _name;
			this->duration = _dur;
			this->downloadTime = 0.0;
			this->contentBuff = NULL;
			this->contentLength = 0;
			this->downloadedBytes = 0;
		}

		ts_file(string _name, float _dur, void *buffer, int length, int _org_length, double _download_time)
		{
			this->status = Downloaded;
			this->name = _name;
			this->duration = _dur;
			this->downloadTime = _download_time;
			this->downloadedBytes = _org_length;
			if (length > 0)
			{
				contentBuff = new char [length];
				//LOGI("1new [%x, %d]", this->contentBuff, length);
				contentLength = length;
				memcpy(contentBuff, buffer, length);
			}
			else 
			{
				contentLength = 0;
				contentBuff = NULL;
			}
		}
		
		~ts_file() 
		{
			if (contentBuff != NULL)
			{
				//LOGI("1delete [%x]", this->contentBuff);
				delete []contentBuff;
				contentBuff = NULL;
			}
		}
};

pthread_mutex_t mutex = PTHREAD_MUTEX_INITIALIZER;
vector<ts_file*> content_file_vector; // used to store ts data get from server
vector<ts_file> playlist_file_info_vector;
string m3u8Cookie;
struct ev_io w_accept;
int tsETag = 0;
bool create_content = false;
int waiting_m3u8_seconds = 0;
bool needStop = false;
int socket_sd = -1;
int sv_port = -1;
int last_error = 0;
int curentTsSocket = -1;

int ttBytes = 0;
int ttDurations = 0;
double ttTimes = 0.0;

//class read thread used to read data when convert
class _read_thread
{
	public:
	pthread_t thread;
	char *buffer;
	int *length;
	int maxLength;
	int pfd;
	
	static void* process(void *context)
	{
		_read_thread *t = (_read_thread*) context;

		fd_set read_flags;
		FD_ZERO(&read_flags);
		FD_SET(t->pfd, &read_flags);

		struct timeval waitd = {1, 0}, begin, end;
		int readBytes = 0, size = 0;

		gettimeofday(&begin, NULL);
		int res;
		while (1)
		{
			if ((res = select(t->pfd + 1, &read_flags, NULL, NULL, &waitd)) > 0)
			{
				if (FD_ISSET(t->pfd, &read_flags))
				{
					readBytes = read(t->pfd, (char*)(t->buffer + size), t->maxLength - size);
					if (readBytes > 0)
					{
						size += readBytes;
						if (size >= t->maxLength)
						{
							size = 0;
							break;
						}
					}
					else
					{
						break;
					}
				}
			}
			else
			{
			}

			gettimeofday(&end, NULL);
			double executionTime = (double) (end.tv_usec - begin.tv_usec) / 1000000.0 + (double) (end.tv_sec - begin.tv_sec);

			if (executionTime > CONVERT_TIMEOUT)
			{
				LOGE("_read_thread timeout = %f; size = %d", executionTime, size);
				size = 0;
				break;
			}
		}

		if (size < 5000)
		{
			size = 0;
		}
		*(t->length) = size;
		pthread_exit(NULL);
		return NULL;
	}
	
	void start()
	{
		pthread_create(&thread, NULL, process, this);
		pthread_setname_np(thread, "read converted data thread");
	}
};

void setNonblock(int fd)
{
	int flags = fcntl(fd, F_GETFL, 0);
	fcntl(fd, F_SETFL, flags | O_NONBLOCK);
}

class _download_content_thread
{
public:
	pthread_t thread;
	bool run;
	char *convertBuffer, *orgBuffer;
	int length, maxLength;
	string fullUrl, currentHostName, cookie;
	double downloadedTime;
	int downloadedBytes;

	int state;

	_download_content_thread()
	{
		this->length = 0;
		this->maxLength = MAX_CONTENT_SIZE;
		this->convertBuffer = new char[maxLength];
		this->orgBuffer = new char [maxLength];
		this->currentHostName = "";
		this->state = STATE_NONE;
		this->run = false;
		this->downloadedTime = 0;
		this->downloadedBytes = 0;
		//LOGI("2new [%x, %d]", this->convertBuffer, maxLength);
		//LOGI("2new [%x, %d]", this->orgBuffer, maxLength);
	}

	~_download_content_thread()
	{
		//LOGI("2delete [%x, %d]", this->convertBuffer, maxLength);
		//LOGI("2delete [%x, %d]", this->orgBuffer, maxLength);
		delete [] this->convertBuffer;
		delete [] this->orgBuffer;
	}

	int getOrgFile(string fullUrl, double downloadTimeout, char *buffer, int &length)
	{
		string hostName, url;
		struct hostent *host;
		parse_link(fullUrl, hostName, url);

		this->downloadedTime = 0.0;
		this->downloadedBytes = 0;
		int content_socket = -1;
		if ((content_socket = socket(AF_INET, SOCK_STREAM, 0)) < 0)
		{
			LOGE("can't create socket");
			return NOT_CREATE;
		}

		struct timeval begin, end;
		gettimeofday(&begin, NULL);

		//LOGI("download org file fullUrl = '%s', socket = %d", fullUrl.c_str(), content_socket);
		struct sockaddr_in server_addr;
		server_addr.sin_family = AF_INET; //IPv4
		server_addr.sin_port = htons(80);

		host = gethostbyname(hostName.c_str());
		if ((host == NULL) || (host->h_addr == NULL))
		{
			LOGE("can't gethostbyname with host = '%s'", hostName.c_str());
			close(content_socket);
			double executeTime = (double) (end.tv_usec - begin.tv_usec) / 1000000.0 + (double) (end.tv_sec - begin.tv_sec);
			ttTimes += (executeTime - this->downloadedTime);
			return NOT_RESOLVE;
		}

		memcpy(&server_addr.sin_addr, host->h_addr, host->h_length);

		setNonblock(content_socket);
		// Trying to connect
		int res = connect(content_socket, (struct sockaddr *)&server_addr, sizeof(server_addr));

		if (res < 0)
		{
			if (errno == EINPROGRESS)
			{
				do
				{
					fd_set myset;
					struct timeval connect_tv;
					connect_tv.tv_sec = 5;
					connect_tv.tv_usec = 0;

					FD_ZERO(&myset);
					FD_SET(content_socket, &myset);
					res = select(content_socket + 1, NULL, &myset, NULL, &connect_tv);
					if (res < 0 && errno != EINTR)
					{
						LOGE("Error connecting %d - %s", errno, strerror(errno));
						close(content_socket);
						double executeTime = (double) (end.tv_usec - begin.tv_usec) / 1000000.0 + (double) (end.tv_sec - begin.tv_sec);
						ttTimes += (executeTime - this->downloadedTime);
						return NOT_CONNECT;
					}
					else if (res > 0)
					{
						// Socket selected for write
						int valopt;
						socklen_t lon = sizeof(int);
						if (getsockopt(content_socket, SOL_SOCKET, SO_ERROR, (void*)(&valopt), &lon) < 0)
						{
							LOGE("Error in getsockopt() %d - %s", errno, strerror(errno));
							close(content_socket);
							double executeTime = (double) (end.tv_usec - begin.tv_usec) / 1000000.0 + (double) (end.tv_sec - begin.tv_sec);
							ttTimes += (executeTime - this->downloadedTime);
							return NOT_CONNECT;
						}

						// Check the value returned...
						if (valopt)
						{
							LOGE("Error in delayed connection() %d - %s", valopt, strerror(valopt));
							close(content_socket);
							double executeTime = (double) (end.tv_usec - begin.tv_usec) / 1000000.0 + (double) (end.tv_sec - begin.tv_sec);
							ttTimes += (executeTime - this->downloadedTime);
							return NOT_CONNECT;
						}
						break;
					}
					else
					{
						LOGE("Timeout in select() - Cancelling!");
						close(content_socket);
						double executeTime = (double) (end.tv_usec - begin.tv_usec) / 1000000.0 + (double) (end.tv_sec - begin.tv_sec);
						ttTimes += (executeTime - this->downloadedTime);
						return NOT_CONNECT;
					}
				} while (1);
			 }
			 else
			 {
				 LOGE("Error connecting %d - %s", errno, strerror(errno));
				 close(content_socket);
				 double executeTime = (double) (end.tv_usec - begin.tv_usec) / 1000000.0 + (double) (end.tv_sec - begin.tv_sec);
				 ttTimes += (executeTime - this->downloadedTime);
				 return NOT_CONNECT;
			 }
		}

		if (!this->run)
		{
			close(content_socket);
			return STOPED;
		}
		/* create request */
		char request[1000];
		sprintf(request, "GET %s HTTP/1.1\r\n"
			"Host: %s\r\n"
			"Connection: close\r\n"
			"User-Agent: stagefright/1.2 (Linux;Android 4.2.2)\r\n"
			"Accept-Encoding: gzip,deflate\r\n\r\n", url.c_str(), hostName.c_str());

		/* send request */
		if (send(content_socket, request, strlen(request), 0) < 0)
		{
			LOGE("can't send request to host '%s' via socket %d", hostName.c_str(), content_socket);
			close(content_socket);
			double executeTime = (double) (end.tv_usec - begin.tv_usec) / 1000000.0 + (double) (end.tv_sec - begin.tv_sec);
			ttTimes += (executeTime - this->downloadedTime);
			return NOT_SEND;
		}

		length = 0;
		int read_bytes = 0, write_bytes = 0;
		char buff[10001], *point;
		bool first = true;

		bool checkHeader = true;

		if (!this->run)
		{
			close(content_socket);
			return STOPED;
		}

		while (1)
		{
			if (!this->run)
			{
				close(content_socket);
				return STOPED;
			}

			fd_set read_flags;
			FD_ZERO(&read_flags);
			FD_SET(content_socket, &read_flags);
			struct timeval waitd = {0, 5000};

			if ((select(content_socket+1, &read_flags, NULL, (fd_set*)0, &waitd)) > 0)
			{
				if (FD_ISSET(content_socket, &read_flags))
				{
					read_bytes = recv(content_socket, buff, 10000, 0);

					if (read_bytes <= 0)
					{
						close(content_socket);
						break;
					}
					else
					{
						this->downloadedBytes += read_bytes;
						ttBytes += read_bytes;
						buff[read_bytes] = 0;
						char *pch;
						if (checkHeader)
						{
							pch = strstr(buff, "HTTP/1.1 200 OK");
							if (pch == NULL)
							{
								close(content_socket);
								pch = strstr(buff, "HTTP/1.1 404");
								if (pch != NULL)
								{
									return NOT_FOUND;
								}
							}
							checkHeader = false;
						}

						pch = strstr(buff, "\r\n\r\n");
						if (first)
						{
							if (pch != NULL)
							{
								point = pch + 4;
								write_bytes = read_bytes - (point - buff);
								first = false;
							}
						}
						else
						{
							point = buff;
							write_bytes = read_bytes;
						}

						if (write_bytes > 0)
						{
							memcpy(buffer + length, point, write_bytes);
							length += write_bytes;
						}
					}
				}
			}

			gettimeofday(&end, NULL);
			double executeTime = (double) (end.tv_usec - begin.tv_usec) / 1000000.0 + (double) (end.tv_sec - begin.tv_sec);
			ttTimes += (executeTime - this->downloadedTime);
			this->downloadedTime = executeTime;
			if (this->downloadedTime > downloadTimeout)
			{
				close(content_socket);
				return SLOW_SPEED;
			}
		}

		if (length > 50000)
			return SUCCESS;
		else
			return SLOW_SPEED;
	}

	int getState()
	{
		return this->state;
	}

	bool convert_ts_file(char* inputBuffer, int inputLength, int maxLength, char *buffer, int &length)
	{
		//LOGI("start convert with url = %s, orgLength = %d", fullTsUrl.c_str(), orgLength);
		int in_pfd[2], out_pfd[2];
		if (pipe(in_pfd) != 0)
		{
			LOGE("can't create pipe");
			return false;
		}

		if (pipe(out_pfd) != 0)
		{
			LOGE("can't create pipe");
			return false;
		}

		setNonblock(in_pfd[1]);
		setNonblock(out_pfd[0]);
		struct timeval begin, end;
		pid_t pid = fork();

		if (pid < 0)
		{
			LOGE("fork() error");
			close(in_pfd[0]);
			close(in_pfd[1]);
			close(out_pfd[1]);
			close(out_pfd[0]);
			return false;
		}
		else if (pid==0)
		{
			LOGI("child-process: call ffmpeg_function");
			// redirect stdin
			if (dup2(in_pfd[0], STDIN_FILENO) == -1) {
				LOGE("redirecting stdin");
				_exit(0);
			}

			// redirect stdout
			if (dup2(out_pfd[1], STDOUT_FILENO) == -1) {
				LOGE("redirecting stdout");
				_exit(0);
			}

			close(in_pfd[0]);
			close(in_pfd[1]);
			close(out_pfd[1]);
			close(out_pfd[0]);

			int argc = 12, i;
			char *argv[12];
			for (i = 0; i < argc; i++)
				argv[i] = (char *)malloc(300);
			//ffmpeg -user-agent VP9TV -i link -bsf h264_mp4toannexb -f mpegts -acodec copy -vcodec copy -copyts -absf aac_adtstoasc -y
			strcpy(argv[0], "ffmpeg");
			strcpy(argv[1], "-i");
			strcpy(argv[2], "pipe:0");
			strcpy(argv[3], "-f");
			strcpy(argv[4], "mpegts");
			strcpy(argv[5], "-c");
			strcpy(argv[6], "copy");
			strcpy(argv[7], "-copyts");
			strcpy(argv[8], "-v");
			strcpy(argv[9], "debug");
			strcpy(argv[10], "-y");
			strcpy(argv[11], "pipe:1");
			LOGI("child-process: call ffmpeg_function");
			ffmpeg_function(argc, argv);
			_exit(0);
		}
		else {
			//LOGI("create children = %d", pid);
			close(in_pfd[0]);
			close(out_pfd[1]);

			int write_bytes = 0;

			_read_thread rt;
			rt.buffer = buffer;
			rt.length = &length;
			rt.maxLength = maxLength;
			rt.pfd = out_pfd[0];
			rt.start();

			gettimeofday(&begin, NULL);

			while (write_bytes < inputLength)
			{
				if (!this->run)
				{
					int resk = kill(pid, SIGKILL);
					close(in_pfd[1]);
					pthread_join(rt.thread, NULL);
					close(out_pfd[0]);
					return false;
				}

				fd_set write_flags;
				FD_ZERO(&write_flags);
				FD_SET(in_pfd[1], &write_flags);
				struct timeval waitd = {1, 0};

				if ((select(in_pfd[1] +1, NULL, &write_flags, NULL, &waitd)) > 0)
				{
					if (FD_ISSET(in_pfd[1], &write_flags))
					{
						int pipeBufLength = 4096;
						int res = write(in_pfd[1], inputBuffer + write_bytes, (inputLength - write_bytes>pipeBufLength)?pipeBufLength:(inputLength-write_bytes));

						if (res > 0)
						{
							write_bytes += res;
						}
						else
						{
							break;
						}
					}
				}

				gettimeofday(&end, NULL);
				double executionTime = (double) (end.tv_usec - begin.tv_usec) / 1000000.0 + (double) (end.tv_sec - begin.tv_sec);

				if (executionTime > CONVERT_TIMEOUT)
				{
					LOGE("convert timeout = %f", executionTime);
					break;
				}
			}

			close(in_pfd[1]);
			pthread_join(rt.thread, NULL);
			close(out_pfd[0]);
			int resk = kill(pid, SIGKILL);
			//LOGI("send kill to pid = %d return res = %d", pid, resk);
			if (write_bytes != inputLength)
			{
				length = 0;
			}
		}

		double ratio = 0;
		if (length > 0)
		{
			ratio = 100.0 * (length - inputLength) / (length);
		}
		gettimeofday(&end, NULL);
		double executionTime = (double) (end.tv_usec - begin.tv_usec) / 1000000.0 + (double) (end.tv_sec - begin.tv_sec);
		double convert_speed = (double) inputLength / (128.0 * executionTime);
		LOGI("convert finish, converted length = %d, ratio = %.2f, convert_speed = %.2f kbps", length, ratio, convert_speed);
		return (length > 0);
	}

	static void* process(void *context)
	{
		_download_content_thread *t = (_download_content_thread*) context;
		t->run = true;

		std::size_t found = t->fullUrl.find_last_of("/");
		string fileName = t->fullUrl.substr(found+1);

		t->fullUrl[t->fullUrl.length()-1] = 'n';
		t->fullUrl[t->fullUrl.length()-2] = 'v';


		int orgLength = 0;

		t->state = STATE_GET_TS;
		int sta = t->getOrgFile(t->fullUrl, VN_DOWNLOAD_TIMEOUT, t->orgBuffer, orgLength);
		t->state = STATE_NONE;

		if ((sta == NOT_FOUND) || (sta == NOT_CONNECT) || (sta == NOT_SEND) || (sta == SLOW_SPEED)
				|| (sta == NOT_CREATE) || (sta == NOT_RESOLVE))
		{
			LOGE("Download url '%s' fail, sta = %d, last_error = %d", t->fullUrl.c_str(), sta, last_error);
			last_error = last_error | sta;
			t->state = STATE_NONE;
			t->run = false;
			pthread_mutex_lock(&mutex);
			for (int i=0; i<content_file_vector.size(); i++)
			{
				if (fileName == content_file_vector[i]->name)
				{
					content_file_vector[i]->status = Fail;
					content_file_vector[i]->downloadedBytes = t->downloadedBytes;
					ttDurations += content_file_vector[i]->duration;
					content_file_vector[i]->downloadTime = t->downloadedTime;
					break;
				}
			}
			pthread_mutex_unlock(&mutex);
			return NULL;
		}
		else
		{
			LOGI("Download success file (%s,%d) in %f, sta = %d", t->fullUrl.c_str(), orgLength, t->downloadedTime, sta);
		}

		if (!t->run)
		{
			return NULL;
		}

		t->state = STATE_CONVERT_TS;
		bool res = t->convert_ts_file(t->orgBuffer, orgLength, t->maxLength, t->convertBuffer, t->length);
		t->state = STATE_NONE;

		//need to lock
		if (!t->run)
		{
			return NULL;
		}
		pthread_mutex_lock(&mutex);
		while (content_file_vector.size() > MAX_OF_TS_FILE_IN_MEMORY)
		{
			delete content_file_vector[0];
			content_file_vector.erase(content_file_vector.begin());
		}

		for (int i=0; i<content_file_vector.size(); i++)
		{
			if (fileName == content_file_vector[i]->name)
			{
				ttDurations += content_file_vector[i]->duration;
				content_file_vector[i]->downloadedBytes = t->downloadedBytes;
				content_file_vector[i]->downloadTime = t->downloadedTime;
				if (res)
				{

					content_file_vector[i]->contentBuff = new char[t->length];
					//LOGI("3new [%x, %d]", content_file_vector[i]->contentBuff, t->length);
					content_file_vector[i]->contentLength = t->length;
					memcpy(content_file_vector[i]->contentBuff, t->convertBuffer, t->length);
					content_file_vector[i]->status = Downloaded;
				}
				else
				{
					LOGE("Fail to convert file %s", fileName.c_str());
					last_error = last_error | NOT_CONVERT;
					content_file_vector[i]->status = Fail;
				}
				break;
			}
		}
		pthread_mutex_unlock(&mutex);
		//need to unlock

		t->run = false;
		//prepare_download_file_vector.erase(prepare_download_file_vector.begin());
		return NULL;
	}

	bool isRunning()
	{
		return this->run;
	}

	void stop()
	{
		LOGI("call download_content_thread stop");
		this->run = false;
		pthread_join(this->thread, NULL);
		LOGI("call download_content_thread stop finish");
	}

	void start()
	{
		pthread_create(&thread, NULL, process, this);
		pthread_setname_np(thread, "download content thread");
		//pthread_detach(thread);
	}
};

class _download_playlist_thread
{
	public:
		pthread_t thread;
		bool run;
		char m3u8Buffer[10000];
		int m3u8ContentLength;
		string fullUrl, cookie;
		string m3u8ETag;
		int state;
		struct hostent *host;
		_download_content_thread download_content_thread[3];
		
		_download_playlist_thread()
		{
			this->run = false;
			this->m3u8ContentLength = 0;
			this->fullUrl = "";
			this->m3u8ETag = "";
			this->host = NULL;
			this->state = 1;
		}
		
		void getCurrentSpeed(int &downloadedBytes, double &downloadedTimes, int &count)
		{
			downloadedBytes = 0;
			downloadedTimes = 0.0;
			count = 0;

			for (int i=0; i<3; i++)
				if (this->download_content_thread[i].isRunning())
				{
					downloadedTimes += this->download_content_thread[i].downloadedTime;
					downloadedBytes += this->download_content_thread[i].downloadedBytes;
					count++;
				}
		}

		int getState()
		{
			return this->state;
		}

		int getOrgFile(string fullUrl, string cookie, double downloadTimeout, string &eTag, char *buffer, int &length, double &download_time)
		{
			LOGI("getOrgFile url = %s", fullUrl.c_str());
			string hostName, url;
			struct hostent *host;
			parse_link(fullUrl, hostName, url);

			int content_socket = -1;
			if ((content_socket = socket(AF_INET, SOCK_STREAM, 0)) < 0)
			{
				LOGE("can't create socket");
				return NOT_CREATE;
			}

			//LOGI("download org file fullUrl = '%s', socket = %d", fullUrl.c_str(), content_socket);
			struct sockaddr_in server_addr;
			server_addr.sin_family = AF_INET; //IPv4
			server_addr.sin_port = htons(80);

			//LOGI("gethostbyname = %s", hostName.c_str());
			host = gethostbyname(hostName.c_str());
			//LOGI("gethostbyname = %s", hostName.c_str());
			if ((host == NULL) || (host->h_addr == NULL))
			{
				LOGE("can't gethostbyname with host = '%s'", hostName.c_str());
				close(content_socket);
				return NOT_RESOLVE;
			}

			memcpy(&server_addr.sin_addr, host->h_addr, host->h_length);

			setNonblock(content_socket);
			// Trying to connect
			int res = connect(content_socket, (struct sockaddr *)&server_addr, sizeof(server_addr));

			if (!this->run)
			{
				close(content_socket);
				return STOPED;
			}

			if (res < 0)
			{
				if (errno == EINPROGRESS)
				{
					//LOGI("EINPROGRESS in connect() - selecting");
					do
					{
						fd_set myset;
						struct timeval connect_tv;
						connect_tv.tv_sec = 2;
						connect_tv.tv_usec = 0;

						FD_ZERO(&myset);
						FD_SET(content_socket, &myset);
						res = select(content_socket + 1, NULL, &myset, NULL, &connect_tv);
						if (res < 0 && errno != EINTR)
						{
							LOGE("Error connecting %d - %s", errno, strerror(errno));
							close(content_socket);
							return NOT_CONNECT;
						}
						else if (res > 0)
						{
							// Socket selected for write
							int valopt;
							socklen_t lon = sizeof(int);
							if (getsockopt(content_socket, SOL_SOCKET, SO_ERROR, (void*)(&valopt), &lon) < 0)
							{
								LOGE("Error in getsockopt() %d - %s", errno, strerror(errno));
								close(content_socket);
								return NOT_CONNECT;
							}

							// Check the value returned...
							if (valopt)
							{
								LOGE("Error in delayed connection() %d - %s", valopt, strerror(valopt));
								close(content_socket);
								return NOT_CONNECT;
							}
							break;
						}
						else
						{
							LOGE("Timeout in select() - Cancelling!");
							close(content_socket);
							return NOT_CONNECT;
						}
					} while (1);
				 }
				 else
				 {
					 LOGE("Error connecting %d - %s", errno, strerror(errno));
					 close(content_socket);
					 return NOT_CONNECT;
				 }
			}

			if (!this->run)
			{
				close(content_socket);
				return STOPED;
			}
			
			/* create request */
			char request[1000];
			if (eTag != "")
			{
				if (cookie != "")
				{
					sprintf(request, "GET %s HTTP/1.1\r\n"
							"Host: %s\r\n"
							"Connection: close\r\n"
							"User-Agent: stagefright/1.2 (Linux;Android 4.2.2)\r\n"
							"Cookie: %s\r\n"
							"If-None-Match: %s\r\n"
							"Accept-Encoding: gzip,deflate\r\n\r\n", url.c_str(), hostName.c_str(), cookie.c_str(), eTag.c_str());
				}
				else
				{
					sprintf(request, "GET %s HTTP/1.1\r\n"
												"Host: %s\r\n"
												"Connection: close\r\n"
												"User-Agent: stagefright/1.2 (Linux;Android 4.2.2)\r\n"
												"If-None-Match: %s\r\n"
												"Accept-Encoding: gzip,deflate\r\n\r\n", url.c_str(), hostName.c_str(), eTag.c_str());
				}
			}
			else
			{
				sprintf(request, "GET %s HTTP/1.1\r\n"
						"Host: %s\r\n"
						"Connection: close\r\n"
						"User-Agent: stagefright/1.2 (Linux;Android 4.2.2)\r\n"
						"Accept-Encoding: gzip,deflate\r\n\r\n", url.c_str(), hostName.c_str());
			}

			/* send request */
			if (send(content_socket, request, strlen(request), 0) < 0)
			{
				LOGE("can't send request to host '%s' via socket %d", hostName.c_str(), content_socket);
				close(content_socket);
				return NOT_SEND;
			}

			length = 0;
			int read_bytes = 0, write_bytes = 0;
			char buff[10001], *point;
			bool first = true;

			struct timeval begin, end;
			gettimeofday(&begin, NULL);
			bool checkHeader = true;
			double executionTime = 0.0;

			while (1)
			{
				fd_set read_flags;
				FD_ZERO(&read_flags);
				FD_SET(content_socket, &read_flags);
				struct timeval waitd = {0, 5000};

				if (!this->run)
				{
					close(content_socket);
					return STOPED;
				}

				if ((select(content_socket+1, &read_flags, NULL, (fd_set*)0, &waitd)) > 0)
				{
					if (FD_ISSET(content_socket, &read_flags))
					{
						read_bytes = recv(content_socket, buff, 10000, 0);

						if (read_bytes <= 0)
						{
							close(content_socket);
							break;
						}
						else
						{
							buff[read_bytes] = 0;
							char *pch;
							if (checkHeader)
							{
								pch = strstr(buff, "HTTP/1.1 200 OK");
								if (pch == NULL)
								{
									close(content_socket);
									pch = strstr(buff, "HTTP/1.1 304");
									if (pch != NULL)
									{
										return NOT_CHANGE;;
									}
									else
									{
										return NOT_FOUND;
									}
								}
								checkHeader = false;

								if (end_with(url, ".m3u8"))
								{
									eTag = "";
									pch = strstr(buff, "ETag: ");
									if (pch != NULL)
									{
										char* pch2 = strstr(pch + 6, "\r\n");
										if (pch2 != NULL)
										{
											for (int i=0; i<(pch2-pch-6); i++)
											{
												eTag += (char)pch[i+6];
											}
										}
									}
								}
							}

							pch = strstr(buff, "\r\n\r\n");
							if (first)
							{
								if (pch != NULL)
								{
									point = pch + 4;
									write_bytes = read_bytes - (point - buff);
									first = false;
								}
							}
							else
							{
								point = buff;
								write_bytes = read_bytes;
							}

							if (write_bytes > 0)
							{
								memcpy(buffer + length, point, write_bytes);
								length += write_bytes;
							}
						}
					}
				}

				gettimeofday(&end, NULL);
				executionTime = (double) (end.tv_usec - begin.tv_usec) / 1000000.0 + (double) (end.tv_sec - begin.tv_sec);
				if (executionTime > downloadTimeout)
				{
					close(content_socket);
					download_time += downloadTimeout;
					return SLOW_SPEED;
				}
			}

			download_time += executionTime;
			return SUCCESS;
		}

		bool get_download_file_list(char *content, int length, vector<ts_file*> &download_file_list)
		{
			int number;
			string ts_link[MAX_OF_TS_LINK];
			float ts_num[MAX_OF_TS_LINK];
			
			content[length] = 0;
			get_ts_link(content, ts_link, ts_num, number);
			
			/*for (int i=0; i<number; i++)
			{
				LOGI("ts_link[%d] = '%s'", i, ts_link[i].c_str());
			}*/
			if (number <= 0) 
			{
				return false;
			}
			
			int index = -1;
			pthread_mutex_lock(&mutex);
			if (download_file_list.size() > 0)
			{
				string lastName = download_file_list[download_file_list.size()-1]->name;
				for (int i=0; i<number; i++) {
					if (lastName == ts_link[i])
					{
						index = i;
						break;
					}
				}
			}

			int startIdx = 0;
			if ((number > NUM_OF_TS_FILE_PER_LIST) && (index == -1))
				startIdx = number - NUM_OF_TS_FILE_PER_LIST;

			for (int i=startIdx; i<number - 1 - index; i++)
			{  
				ts_file *temp = new ts_file();
				temp->name = string(ts_link[index+1+i]);
				temp->status = Waiting;
				temp->duration = (ts_num[index+1+i]>0)?(ts_num[index+1+i]):1;
				LOGI("push file '%s:%.3f' to download file list", temp->name.c_str(), temp->duration);
				for (int j=0; j<download_file_list.size(); j++)
				{
					if (download_file_list[j]->name == temp->name)
					{
						delete download_file_list[j];
						download_file_list.erase(download_file_list.begin() + j);
						break;
					}
				}
				download_file_list.push_back(temp);
			}
			
			//LOGI("size of list = %d", download_file_list.size());
			pthread_mutex_unlock(&mutex);
			return true;
		}
		
		void stop()
		{
			LOGI("call stop");
			this->run = false;
			this->state = STATE_STOPPING;
			for (int i=0; i<3; i++)
				if (this->download_content_thread[i].isRunning())
				{
					LOGI("call stop thread %d", i);
					this->download_content_thread[i].stop();
				}

			pthread_join(this->thread, NULL);
			this->state = STATE_NONE;
			LOGI("call stop finish");
		}

		bool isRunning()
		{
			return this->run;
		}
		
		static void* process(void *context)
		{
			_download_playlist_thread *t = (_download_playlist_thread*) context;
			t->run = true;
			t->m3u8ETag = "";
			
			unsigned found = t->fullUrl.find_last_of('/');
			string prefixUrl = t->fullUrl.substr(0, found);
			
			last_error = SUCCESS;
			while (t->run)
			{
				int retryLoadNumber = 0;
				while (t->run)
				{
					double temp_time;
					t->state = STATE_GET_M3U8;
					int sta = t->getOrgFile(t->fullUrl, t->cookie, M3U8_DOWNLOAD_TIMEOUT, t->m3u8ETag, t->m3u8Buffer, t->m3u8ContentLength, temp_time);
					t->state = STATE_NONE;
					if ((sta == NOT_FOUND) || (sta == NOT_CONNECT) || (sta == NOT_SEND) || (sta == SLOW_SPEED)
							|| (sta == NOT_CREATE) || (sta == NOT_RESOLVE))
					{
						LOGE("Download url '%s' fail, sta = %d, last_error = %d", t->fullUrl.c_str(), sta, last_error);
						last_error = last_error | sta;
						if (t->run)
						{
							usleep(900000); //0.3s
						}
					}
					else if (last_error == NOT_CHANGE)
					{
						retryLoadNumber++;
						if (retryLoadNumber > 8)
						{
							last_error = last_error | sta;
							LOGE("Very long not changed with url '%s'", t->fullUrl.c_str());
						}
						else
						{
							last_error = SUCCESS;
						}

						if (t->run)
						{
							usleep(500000); //0.5s
						}
					}
					else //STOPPING and success
					{
						last_error = SUCCESS;
						break;
					}
				}
				
				if (!t->run)
				{
					break;
				}

				last_error = SUCCESS;
				t->get_download_file_list(t->m3u8Buffer, t->m3u8ContentLength, content_file_vector);

				pthread_mutex_lock(&mutex);
				double duration = (content_file_vector.size()<=0)?1.0:content_file_vector[content_file_vector.size()-1]->duration;
				pthread_mutex_unlock(&mutex);

				for (int i=0; i<(int)duration*2; i++)
				{
					if (!t->run) break;
					pthread_mutex_lock(&mutex);
					for (int k=0; k<content_file_vector.size(); k++)
					{
						if (content_file_vector[k]->status == Waiting)
						{
							for (int j=0; j<3; j++)
							{
								if (!t->download_content_thread[j].isRunning())
								{
									t->download_content_thread[j].fullUrl = prefixUrl + "/" + content_file_vector[k]->name;
									t->download_content_thread[j].run = true;
									content_file_vector[k]->status = Downloading;
									t->download_content_thread[j].start();
									break;
								}
							}
						}
					}

					pthread_mutex_unlock(&mutex);
					usleep(500000); //0.5s
				}
			}

			//LOGI("pthread_exit _process_thread");
			pthread_exit(NULL);
			return NULL;
		}
		
		void start()
		{
			pthread_create(&thread, NULL, process, this);
			pthread_setname_np(thread, "download playlist thread");
			//pthread_detach(thread);
		}
};

class _connection {
	public:
		int socketfd;
		struct ev_io *w_ev_io;
		string type;
		string url;
		char buffer[BUFFER_SIZE];
		int length;
		bool connected;
		clock_t time;
	public:
		_connection() 
		{
			connected = false;
			socketfd = -1;
			w_ev_io = NULL;
			memset(buffer, 0, BUFFER_SIZE);
			length = 0;
			type = "";
			url = "";
		}

		void clear() 
		{
			connected = false;
			w_ev_io = NULL;
			socketfd = -1;
			memset(buffer, 0, BUFFER_SIZE);
			type = "";
			url = "";
			length = 0;
		}
};

struct ev_loop *main_loop;
struct ev_timer mytimer;
_connection connection[MAX_CONNECTION];
string newFullUrl, oldFullUrl = "";
_download_playlist_thread dt;
int m3u8ETag = 0;

int get_connection_index(int socketfd) 
{
	for (int i=0; i<MAX_CONNECTION; i++)
	{
		if (connection[i].socketfd == socketfd) 
		{
			return i;
		}
	}
	
	return -1;
}

void create_error_m3u8_content()
{
	LOGI("create error m3u8");
	char *buf = (char *)malloc(1000);
	int buf_size = sprintf(buf, "%sConnection: close\r\n\r\n", "HTTP/1.1 404 Not Found\r\n");

	for (int i=0; i<MAX_CONNECTION; i++)
	{
		if ((connection[i].type == "m3u8"))
		{
			//LOGI("create_error_m3u8_content return len = %d back to client %d", strlen(buf), connection[i].socketfd);
			int sent = send(connection[i].socketfd, buf, strlen(buf), 0);
			//LOGI("create_error_m3u8_content return len = %d back to client %d finish, sent = %d", strlen(buf), connection[i].socketfd, sent);
		}
	}

	//LOGI("---create_error_m3u8_content finish");
	free(buf);
}

string last_file_in_playlist = "";
int number_nochange_playlist = 0;
int media_sequence = 0;
void create_new_m3u8_content() 
{

	LOGI("---create_new_m3u8_content");
	pthread_mutex_lock(&mutex);
	//LOGI("---create_new_m3u8_content lock finish");

    int size = content_file_vector.size(), i;
	
	char *contentBuffer = (char *)malloc(1000);
	char *p = contentBuffer;
	memset(contentBuffer, 0, 1000);

	int index = -1;
	bool newList = false;
	if (last_file_in_playlist != "")
	{
		for (int i=0; i<size; i++)
			if (content_file_vector[i]->name == last_file_in_playlist)
			{
				index = i;
				break;
			}
	}

	//LOGI("index = %d", index);
	if (index < 0)
	{
		if (size < NUM_OF_TS_FILE_PER_LIST)
		{
			last_file_in_playlist = "";
			pthread_mutex_unlock(&mutex);
			create_error_m3u8_content();
			return;
		}
		else
		{
			newList = true;
			media_sequence = 0;
			last_file_in_playlist = content_file_vector[size-1]->name;
			playlist_file_info_vector.clear();
			for (int i=size-5; i<size; i++)
			{
				playlist_file_info_vector.push_back(ts_file(content_file_vector[i]->name, content_file_vector[i]->duration));
			}
		}
	}
	else if (index < size -1)
	{
		int idx = index + 1;
		while (idx < size)
		{
			if (content_file_vector[idx]->status == Downloaded)
			{
				//push file to list
				//LOGI("idx = %d", idx);
				playlist_file_info_vector.erase(playlist_file_info_vector.begin());
				playlist_file_info_vector.push_back(ts_file(content_file_vector[idx]->name, content_file_vector[idx]->duration));
				last_file_in_playlist = content_file_vector[idx]->name;
				media_sequence++;
				newList = true;
				break;
			}
			else if (content_file_vector[idx]->status == Downloading)
			{
				break;
			}
			//fail, process next file
			else
			{
				idx++;
			}
		}
	}
	pthread_mutex_unlock(&mutex);

	//LOGI("newList = %d", newList);
	if (newList == false)
	{
		number_nochange_playlist++;
		if (number_nochange_playlist >= MAX_NUMBER_NOCHANGE_PLAYLIST)
		{
			//number_nochange_playlist = 0;
			create_error_m3u8_content();
			return;
		}
	}
	else
	{
		number_nochange_playlist = 0;
	}
		
	float max = 1.0;
	for (i = 0; i< playlist_file_info_vector.size(); i++)
	{
		if (playlist_file_info_vector[i].duration > max)
		{
			max = playlist_file_info_vector[i].duration;
		}	
	}
	sprintf(contentBuffer, "%s%d%s%d\n", "#EXTM3U\n#EXT-X-VERSION:3\n#EXT-X-TARGETDURATION:", (int)max , "\n#EXT-X-MEDIA-SEQUENCE:", media_sequence+1);
	
	string temp = playlist_file_info_vector[0].name + " --> " + playlist_file_info_vector[playlist_file_info_vector.size()-1].name;
	LOGI("create_new_m3u8_content return new m3u8 content: %s, media_sequence = %d", temp.c_str(), media_sequence);
	for (i = 0; i<playlist_file_info_vector.size(); i++) {
		p = contentBuffer + strlen(contentBuffer);
		sprintf(p, "%s%.3f,\n%s\n", "#EXTINF:", playlist_file_info_vector[i].duration, playlist_file_info_vector[i].name.c_str());
	}

	char *buf = (char *)malloc(1000);
	memset(buf, 0, 1000);
	
	sprintf(buf, "%s%s%d%s%s%d%s%s%s", "HTTP/1.1 200 OK\r\n",
							"Server: Apache\r\nLast-Modified: Mon, 02 Feb 2015 07:55:16 GMT\r\nETag: \"fb-50e1646660d", m3u8ETag++, "\"\r\nAccept-Ranges: bytes\r\n",
							"Content-Length: ", strlen(contentBuffer), "\r\n",
							"Access-Control-Allow-Origin: *\r\nConnection: close\r\nContent-Type: application/x-mpegURL\r\n\r\n",
							contentBuffer);
	
	//LOGI("request = %s", buf);
	for (i=0; i<MAX_CONNECTION; i++) 
	{
		if ((connection[i].type == "m3u8") && (connection[i].connected))
		{
			//LOGI("prepare send back to client %d", connection[i].socketfd);
			int res = send(connection[i].socketfd, buf, strlen(buf), 0);
			connection[i].connected = false;
			//LOGI("send back to client %d %s %d/%d", connection[i].socketfd, buf, res, strlen(buf));
		}

	}
	
	free(buf);
	free(contentBuffer);
}

int create_listen_socket(char* listen_ip, int listen_port) {
	int socket_sd = -1;

	if ((socket_sd = socket(AF_INET, SOCK_STREAM, 0)) < 0) {
		LOGE("Can't create socket, error:%s", strerror(errno));
		return -1;
	}

	struct sockaddr_in server_addr;
	server_addr.sin_family = AF_INET; //IPv4
	server_addr.sin_port = htons(listen_port);
	server_addr.sin_addr.s_addr = htonl(INADDR_ANY);

	int flag = 1;
	setsockopt(socket_sd, SOL_SOCKET, SO_REUSEADDR, (char *) &flag, sizeof(int));

	// Bind client socket with client address
	if (bind(socket_sd, (struct sockaddr *) &server_addr, sizeof(server_addr)) != 0) {
		LOGE("Can't bind client socket");
		return -1;
	}

	// Listen to client socket
	if (listen(socket_sd, MAX_CONNECTION) < 0) {
		LOGE("Listen to control socket failed, error:%s", strerror(errno));
		return -1;
	}

	return socket_sd;
}

int process_ts_request(int socket);
static void one_second_cb (struct ev_loop *loop, struct ev_timer *w, int revents)
{
	//LOGI("one_second_cb");
	if (needStop)
	{
		LOGI("stop process...");
		ev_io_stop(main_loop, &w_accept);

		for (int i=0; i<MAX_CONNECTION; i++)
		{
			if (connection[i].w_ev_io != NULL)
			{
				LOGI("stop socket %d", connection[i].socketfd);
				ev_io_stop(main_loop, connection[i].w_ev_io);
				free(connection[i].w_ev_io);
				close(connection[i].socketfd);
				connection[i].clear();
			}
		}

		if (socket_sd != -1)
		{
			LOGI("stop server socket (%d:%d)", socket_sd, sv_port);
			if (shutdown(socket_sd, SHUT_RDWR) < 0) // secondly, terminate the 'reliable' delivery
				if (errno != ENOTCONN && errno != EINVAL) // SGI causes EINVAL
					LOGE("shutdown error %d", errno);
			if (close(socket_sd) < 0) // finally call close()
					LOGE("close error %d", errno);
			socket_sd = -1;
			LOGI("stop server socket (%d) finish", sv_port);
		}

		if (dt.isRunning())
		{
			dt.stop();
			for (int i=0; i<content_file_vector.size(); i++)
			{
				delete content_file_vector[i];
			}
			content_file_vector.clear();
		}
		//LOGI("pexit process...");
		ev_unloop(main_loop, EVUNLOOP_ALL);
		LOGI("pthread_exit _main_thread");
		return;
	}

	if (create_content)
	{
		if (content_file_vector.size() >= NUM_OF_TS_FILE_PER_LIST)
		{
			create_content = false;
			create_new_m3u8_content();
		}
		else
		{
			waiting_m3u8_seconds++;
			if (waiting_m3u8_seconds >= M3U8_RESPONCE_TIMEOUT)
			{
				create_content = false;
				waiting_m3u8_seconds = 0;
				create_error_m3u8_content();
			}
		}
	}

	if (curentTsSocket != -1)
	{
		process_ts_request(curentTsSocket);
	}

	if (dt.isRunning())
	{
		int state = dt.getState();
		if (state == STATE_GET_M3U8)
		{
			LOGI("getting m3u8 content ...");
		}
		else if (state == STATE_GET_TS)
		{
			LOGI("getting ts content ...");
		}
		else if (state == STATE_CONVERT_TS)
		{
			LOGI("converting ts content ...");
		}
		else if (state == STATE_NONE)
		{
			LOGI("waiting ...");
		}
	}
} 
void process_m3u8_request()
{
	for (int i=0; i<MAX_CONNECTION; i++)
	{
		if ((connection[i].type == "m3u8") && (connection[i].url != newFullUrl)) 
		{
			LOGI("-------close client %d------------- by AI", connection[i].socketfd);
			close(connection[i].socketfd);
			ev_io_stop(main_loop, connection[i].w_ev_io);
			free(connection[i].w_ev_io);
			connection[i].clear();
		}
	}
	
    //same as with old url
    if (newFullUrl != oldFullUrl)
    {
		if (dt.isRunning())
		{
			dt.stop();
		}
    	oldFullUrl = newFullUrl;
		dt.fullUrl = newFullUrl;
		dt.cookie = m3u8Cookie;

		for (int i=0; i<content_file_vector.size(); i++)
		{
			delete content_file_vector[i];
		}
		content_file_vector.clear();
		playlist_file_info_vector.clear();
		last_file_in_playlist = "";
		number_nochange_playlist = 0;
		create_content = true;
		ttTimes = 0;
		ttDurations = 0;
		ttBytes = 0;
		dt.start();
    }
	else
	{
		if (!create_content)
		{
			create_new_m3u8_content();
		}
	}
}

int process_ts_request(int socket)
{
	std::size_t found = newFullUrl.find_last_of("/");
	string fileName = newFullUrl.substr(found+1);
	
	char *buf = NULL;
	int buf_size = 0;

	LOGI("process_ts_request with fileName = %s", fileName.c_str());
	pthread_mutex_lock(&mutex);
	if (content_file_vector.size() >= 1)
	{
		ts_file *temp_file = NULL;
		bool flag = false;

		string lastFileName = content_file_vector.back()->name;
		float duration = 0.0;
		for (int i = 0; i<content_file_vector.size(); i++)
		{
			if (content_file_vector[i]->name == fileName)
			{
				LOGI("content_file_vector[i]->status = %d", content_file_vector[i]->status);
				if (content_file_vector[i]->status == Downloaded)
				{
					temp_file = content_file_vector[i];
					duration = temp_file->duration;
					flag = true;
				}
				else if ((content_file_vector[i]->status == Downloading) || (content_file_vector[i]->status == Waiting))
				{
					//pending the request
					curentTsSocket = socket;
					pthread_mutex_unlock(&mutex);
					return false;
				}

				curentTsSocket = -1;
				break;
			}
		}
	
		if (flag)
		{
			int size = temp_file->contentLength;
			size = size - size % 188;
			if (size > 0)
			{
				buf = (char *)malloc(1000 + size);
				int header_len = sprintf(buf, "%s%s%s%s%d%s%s%s%d%s%s%s\r\n",
                    "HTTP/1.1 200 OK\r\n",
                    "Server: Apache\r\n",
                    "Last-Modified: Mon, 02 Feb 2015 08:16:36 GMT\r\n",
                    "ETag: \"4a9f8-50e1692aad", tsETag++, "4eb\"\r\n",
                    "Accept-Ranges: bytes\r\n",
                    "Content-Length: ", size, "\r\n",
                    "Access-Control-Allow-Origin: *\r\n",
                    "Connection: close\r\n"
                );
        
				memcpy(buf + header_len, temp_file->contentBuff, temp_file->contentLength);
				buf_size = header_len + size;
				LOGI("return fileName = '%s/%s:%.2f' with size %d to client %d", fileName.c_str(), lastFileName.c_str(), duration, size, socket);
			}
		}
	}
	pthread_mutex_unlock(&mutex);

	int send_bytes = 0, total_bytes = 0;
	if (buf_size == 0)
	{
		LOGE("process_ts_request return 404");
		buf = (char *)malloc(1000);
		buf_size = sprintf(buf, "%sConnection: close\r\n\r\n", "HTTP/1.1 404 Not Found\r\n");
	}

	while (total_bytes < buf_size)
	{
		send_bytes = send(socket, buf + total_bytes, buf_size - total_bytes, MSG_NOSIGNAL);
		if (send_bytes > 0)
		{
			total_bytes += send_bytes;
		}
		else
		{
			break;
		}
	}
		
	free(buf);
    return true;
}

void read_client_connection(struct ev_loop *loop, struct ev_io *watcher, int revents) {
	if (EV_ERROR & revents) {
		LOGE("^^^^____Get invalid event in while read server connection___^^^^");
		//ev_io_stop(loop, watcher);
		//free(watcher);
		return;
	}

	//LOGI("read_client_connection");
    int connection_index = get_connection_index(watcher->fd);
    if (connection_index < 0) 
    {
		LOGE("connection_index of %d = -1", watcher->fd);
        return;
    }
    
	//LOGI("read_client_connection %d", watcher->fd);
	int read = recv(watcher->fd, connection[connection_index].buffer + connection[connection_index].length, 
					BUFFER_SIZE - connection[connection_index].length, 0);
	LOGI("read_client_connection %d %d bytes", watcher->fd, read);
	if (read <= 0) {
		//LOGI("-------close client %d------------- by client", watcher->fd);
		close(watcher->fd);
		ev_io_stop(loop, watcher);
		free(watcher);
        connection[connection_index].clear();
	}
	else {
        connection[connection_index].length += read;
		//LOGI("req = %s", connection[connection_index].buffer);
		if (strstr(connection[connection_index].buffer, "\r\n\r\n") != NULL) {
			if (get_link(connection[connection_index].buffer, newFullUrl, m3u8Cookie)) {
				LOGI("read link %s from client %d", newFullUrl.c_str(), watcher->fd);
				if (end_with(newFullUrl, ".m3u8")) {
					connection[connection_index].type = "m3u8";
					connection[connection_index].url = newFullUrl;
					process_m3u8_request();
					return;
				}
				else if ((end_with(newFullUrl, ".ts")) || (end_with(newFullUrl, ".vn")))
				{
					connection[connection_index].type = "ts";
                    process_ts_request(watcher->fd);
				}
				/*else if (end_with(video_link, ".mp4")){
					process_request();
				}*/
			}
		}
	}
}

void signal_callback_handler(int signum)
{
	LOGE("Caught signal %d\n", signum);
	/*for (int i=0; i<MAX_CONNECTION; i++)
	{
		if (connection[i].w_ev_io != NULL)
		{
			close(connection[i].socketfd);
			ev_io_stop(main_loop, connection[i].w_ev_io);
			free(connection[i].w_ev_io);
			connection[i].clear();
		}
	}

	//close socket server
	if (socket_sd != -1)
		close(socket_sd);
	LOGI("pthread_exit _main_thread");
	needStop = true;*/
	//exit(0);
}

void accept_client_connection(struct ev_loop *loop, struct ev_io *watcher, int revents) {
	if (EV_ERROR & revents)
	{
		LOGE("Get invalid event from function: \"accept_client_connection\"");
		return;
	}

	struct sockaddr_in client_addr;
	socklen_t client_len = sizeof(client_addr);
	int client_sd = accept(watcher->fd, (struct sockaddr *) &client_addr, &client_len);

	if (client_sd < 0)
	{
		LOGE("Accept control connection failed");
		return;
	}
	
    for (int i=0; i<MAX_CONNECTION; i++)
	{
        if (connection[i].w_ev_io == NULL)
		{
            connection[i].socketfd = client_sd;
            connection[i].w_ev_io = (struct ev_io*) malloc(sizeof(struct ev_io));
            ev_io_init(connection[i].w_ev_io, read_client_connection, client_sd, EV_READ);
            ev_io_start(main_loop, connection[i].w_ev_io);
			connection[i].time = clock();
			connection[i].connected = true;
            break;
        }
    }
	
	LOGI("Accept client %d", client_sd);
	for (int i=0; i<MAX_CONNECTION; i++)
	{
		if (connection[i].w_ev_io != NULL) {
			if (((float)(clock()-connection[i].time)/CLOCKS_PER_SEC) > 30.0) {
				LOGI("-------close client %d------------- by connection timeout", connection[i].socketfd);
				close(connection[i].socketfd);
				ev_io_stop(main_loop, connection[i].w_ev_io);
				free(connection[i].w_ev_io);
				connection[i].clear();
			}
			else
			{
				//LOGI("****** connection[%d].socketfd = %d, type = %s, time_diff = %f", i, connection[i].socketfd, connection[i].type.c_str(), (float)(clock()-connection[i].time)/10000);
			}
		}
	}
}

#ifdef _ANDROID_
jint native_start_proxy(JNIEnv *pEnv, jobject pObj) {
#else
int main() {
#endif
	LOGI("start proxy version %s...", PROXY_VERSION);
	
	for (int i=1; i<=SIGRTMAX; i++)
		signal(i, signal_callback_handler);
	signal(SIGPIPE, SIG_IGN);

	main_loop = ev_default_loop(0);

	socket_sd = create_listen_socket((char*)"127.0.0.1", 0);
	if (socket_sd < 0) {
		LOGE("Create listen socket fail!");
		return -1;
	}

	struct sockaddr_in sin;
	socklen_t len = sizeof(sin);
	if (getsockname(socket_sd, (struct sockaddr *)&sin, &len) == -1)
		sv_port = -1;
	else
	    sv_port =  ntohs(sin.sin_port);

	LOGI("proxy is listening port %d", sv_port);

	ev_io_init(&w_accept, accept_client_connection, socket_sd, EV_READ);
	ev_io_start(main_loop, &w_accept);

	ev_timer_init (&mytimer, one_second_cb, 0., 1.);
	ev_timer_start (main_loop, &mytimer);
	
	ev_loop(main_loop, 0);
	return 0;
}

#ifdef _ANDROID_
jint native_get_port(JNIEnv *pEnv, jobject pObj)
{
	return sv_port;
}

jdouble native_get_durations(JNIEnv *pEnv, jobject pObj, jint index)
{
	if ((!dt.isRunning()) || (dt.getState() == STATE_STOPPING))
	{
		return 0.0;
	}

	if (index == 0)
	{
		int size = content_file_vector.size();
		for (int i = size-1; i>=0; i--)
		{
			if (content_file_vector[i]->status != Waiting)
			{
				return content_file_vector[i]->duration;
			}
		}
	}
	else if (index == 1)
	{
		int size = content_file_vector.size();
		double res = 0.0;
		int count = 0;
		for (int i = size-1; i>=0; i--)
		{
			if (content_file_vector[i]->status != Waiting)
			{
				res += content_file_vector[i]->duration;
				count++;
			}

			if (count == 5)
			{
				break;
			}
		}
		return res;
	}
	else
	{
		return ttDurations;
	}

	return 1.0;
}

jdouble native_get_speed(JNIEnv *pEnv, jobject pObj, jint index)
{
	if ((!dt.isRunning()) || (dt.getState() == STATE_STOPPING))
	{
		return 0.0;
	}

	double times;
	int bytes, count;
	dt.getCurrentSpeed(bytes, times, count);

	//last speed
	if (index == 0)
	{
		int size = content_file_vector.size();
		for (int i = size-1; i>=0; i--)
		{
			if ((content_file_vector[i]->status == Downloaded) || (content_file_vector[i]->status == Fail))
			{
				bytes += content_file_vector[i]->downloadedBytes;
				times += content_file_vector[i]->downloadTime;
				break;
			}
		}

		if (times > 0.0)
		{
			return (double)(bytes) / (128.0 * times);
		}
	}
	else if (index == 1)
	{
		int size = content_file_vector.size();
		for (int i = size-1; i>=0; i--)
		{
			if ((content_file_vector[i]->status == Downloaded) || (content_file_vector[i]->status == Fail))
			{
				bytes += content_file_vector[i]->downloadedBytes;
				times += content_file_vector[i]->downloadTime;
				count++;
			}

			if (count == 5)
			{
				break;
			}
		}

		if (times > 0.0)
		{
			return (double)(bytes) / (128.0 * times);
		}
	}
	else
	{
		if (ttTimes > 0)
			return (double)(ttBytes) / (128.0 * ttTimes);
	}

	return 0.0;
}

jdouble native_get_expected_speed(JNIEnv *pEnv, jobject pObj, jint index)
{
	if ((!dt.isRunning()) || (dt.getState() == STATE_STOPPING))
	{
		return 0.0;
	}

	if (index == 0)
	{
		int size = content_file_vector.size();
		for (int i = size-1; i>=0; i--)
		{
			if (content_file_vector[i]->status == Downloaded)
			{
				return (double)(content_file_vector[i]->downloadedBytes) / (128.0 * content_file_vector[i]->duration);
			}
		}
	}
	else if (index == 1)
	{
		int size = content_file_vector.size();
		int bytes = 0, count = 0;
		double times = 0.0;
		for (int i = size-1; i>=0; i--)
		{
			if (content_file_vector[i]->status == Downloaded)
			{
				bytes += content_file_vector[i]->downloadedBytes;
				times += content_file_vector[i]->duration;
				count++;
			}

			if (count == 5)
			{
				break;
			}
		}
		if (times > 0.0)
		{
			return (double)(bytes) / (128.0 * times);
		}
	}
	else
	{
		if (ttDurations > 0)
			return (double)(ttBytes) / (128.0 * ttDurations);
	}

	return 0.0;
}

jint native_stop(JNIEnv *pEnv, jobject pObj)
{
	LOGI("call native_stop");
	needStop = true;
	return 1;
}

jint native_get_last_error(JNIEnv *pEnv, jobject pObj)
{
	return (jint)last_error;
}

jint native_get_number_connection(JNIEnv *pEnv, jobject pObj)
{
	int count = 0;
	for (int i=0; i<3; i++)
		if (dt.download_content_thread[i].isRunning())
		{
			count++;
		}
	return count;
}

jint native_get_number_segment(JNIEnv *pEnv, jobject pObj)
{
	int count = 0;
	for (int i=0; i<3; i++)
		if (dt.download_content_thread[i].isRunning())
		{
			count++;
		}

	pthread_mutex_lock(&mutex);
	for (int i=0; i<content_file_vector.size(); i++)
	{
		if (content_file_vector[i]->status == Waiting)
		{
			count++;
		}
	}
	pthread_mutex_unlock(&mutex);
	return count;
}

jint native_get_bytes(JNIEnv *pEnv, jobject pObj)
{
	return ttBytes;
}

jdouble native_get_download_time(JNIEnv *pEnv, jobject pObj)
{
	return ttTimes;
}

#endif
