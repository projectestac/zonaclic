FROM php:7.4-fpm

RUN DEBIAN_FRONTEND=noninteractive \
    apt-get update && \
    apt-get install -y libzip-dev && \
    rm -rf /var/lib/apt/lists/* && \
    docker-php-ext-install pdo pdo_mysql zip
