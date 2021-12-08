FROM php:7.4-fpm

RUN apt-get update && apt-get install -y apt-utils libzip-dev zip
RUN docker-php-ext-install pdo pdo_mysql zip
