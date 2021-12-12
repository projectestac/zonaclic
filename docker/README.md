# Docker containers for "zonaClic" site and API

__WARNING__: This docker containers are provided for development purposes only. Don't use them in production!

Another development environment is also provided, based on [Devilbox](http://devilbox.org/). See [`/devilbox`](../devilbox) for details.

In order to provide all the necessary services, four different containers are used:
- __nginx__: Currently, the official [`nginx:1.21`](https://hub.docker.com/_/nginx) image is used. It exposes the static files stored in `/web`, and redirects the special paths(`/db`, `/jnlp`, `/redirect`, `/ca`, `/es` and `/en` to the PHP-FPM container via [fastcgi](https://en.wikipedia.org/wiki/FastCGI). See [`nginx/clic-nginx.conf`](./nginx/clic-nginx.conf) for details.
- __php__: A specific PHP-FPM container, built upon the official [`php:7.4-fpm`](https://hub.docker.com/_/php) image. See [`php/Dockerfile`](./php/Dockerfile) for details.
- __mysql__: The official [`mysql:5.7`](https://hub.docker.com/_/mysql) image, initialized with the database scheme needed by the full-text search API. See [`mysql/initdb`](./mysql/initdb) for details.
- __sqid__: Image [`datadog/squid`](https://hub.docker.com/r/datadog/squid), used to emulate the production site of `clic.xtec.cat`, where a proxy should used by the PHP engine to establish external connections.

## How to use it

A SSL key and certificate should be generated. You can use a valid certificate, or create a _fake_ one just for development purposes. In order to create such certificate, [OpenSSL](https://www.openssl.org/) should be installed on your system:

```bash
$ cd <project root>/docker/nginx/cert
$ openssl req -x509 -newkey rsa:4096 -keyout key.pem -out cert.pem -sha256 -days 365 -subj "/CN=clic.xyz" -nodes
```
Another importrant step is to allow the PHP process to read and write into `web/users`, because here is where the API will upload and delete user's projects. This can ve achieved changing the ownership of this directory to the system user (usually `www-data`), or allowing write permissions to all system users:

```bash
$ cd <project root>/web
$ chmod -R og+w users
```

In order to access the site, the host name `clic.xyz` should be locally assigned to your _localhost_ IP. This can be achieved by placing this line in `/etc/hosts`:

```
127.0.0.1    localhost clic.xyz
```

The containers can be set up and launched together with [Docker-Compose](https://docs.docker.com/compose/). Just enter this directory and launch:

```bash
$ cd <project root>/docker
$ docker-compose up --build
```

The web site will be then available at: https://clic.xyz/

If you are using a _fake_ SSL certificate, the browser will warn you of a possible impersonation with the NET::ERR_CERT_AUTHORITY_INVALID error code. You can trust the certificate only for the current session (in advanced settings), or install it as a trusted source in your browser options.

Data and logs will be persisted inside the `log` and `data` folders, located inside each container subdirectory.
