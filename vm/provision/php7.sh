#!/bin/bash

wwwdir=/vm/web

echo 'Install PHP 7'

sudo apt-get install -y apache2 php7.0 php7.0-curl php7.0-json php7.0-mysql php7.0-cli php7.0-zip libapache2-mod-php7.0 ssl-cert

sudo cp /vm/provision/sites/* /etc/apache2/sites-available
sudo a2ensite clic

echo "ServerName localhost" | sudo tee /etc/apache2/conf-available/fqdn.conf
sudo a2enconf fqdn

sudo a2enmod ssl
sudo a2enmod rewrite
sudo a2ensite clic-ssl

#PHP Apache Configuration
sudo sed -i '$ a\date.timezone = "Europe/Andorra"' /etc/php/7.0/apache2/php.ini
sudo sed -i "s/error_reporting = .*/error_reporting = E_ALL/" /etc/php/7.0/apache2/php.ini
sudo sed -i "s/memory_limit = .*/memory_limit = 256M/" /etc/php/7.0/apache2/php.ini
sudo sed -i "s/display_errors = .*/display_errors = On/" /etc/php/7.0/apache2/php.ini
sudo sed -i "s/display_startup_errors = .*/display_startup_errors = On/" /etc/php/7.0/apache2/php.ini
sudo sed -i "s/post_max_size = .*/post_max_size = 100M/" /etc/php/7.0/apache2/php.ini
sudo sed -i "s/upload_max_filesize = .*/upload_max_filesize = 100M/" /etc/php/7.0/apache2/php.ini
sudo sed -i "s/;error_log = php_errors.log/error_log = \/var\/log\/apache2\/php_errors.log/" /etc/php/7.0/apache2/php.ini
sudo sed -i "s/max_execution_time = .*/max_execution_time = 300/" /etc/php/7.0/apache2/php.ini
sudo sed -i "s/allow_url_fopen = .*/allow_url_fopen = On/" /etc/php/7.0/apache2/php.ini

#PHP cli configuration
sudo sed -i '$ a\date.timezone = "Europe/Andorra"' /etc/php/7.0/cli/php.ini
sudo sed -i "s/error_reporting = .*/error_reporting = E_ALL/" /etc/php/7.0/cli/php.ini
sudo sed -i "s/memory_limit = .*/memory_limit = 256M/" /etc/php/7.0/cli/php.ini
sudo sed -i "s/display_errors = .*/display_errors = On/" /etc/php/7.0/cli/php.ini
sudo sed -i "s/display_startup_errors = .*/display_startup_errors = On/" /etc/php/7.0/cli/php.ini
# Next line is commented on cli to allow composer install
#sudo sed -i "s/allow_url_fopen = .*/allow_url_fopen = Off/" /etc/php/7.0/cli/php.ini
sudo sed -i "s/;error_log = php_errors.log/error_log = \/var\/log\/apache2\/php_errors.log/" /etc/php/7.0/cli/php.ini
sudo sed -i "s/max_execution_time = .*/max_execution_time = 300/" /etc/php/7.0/cli/php.ini
#sudo sed -i "s/disable_functions = .*/disable_functions = /" /etc/php/7.0/cli/php.ini

# Add user "ubuntu" to www-data
sudo adduser ubuntu www-data

#Log
sudo sed -i "s/create 640.*/create 777 ubuntu ubuntu/" /etc/logrotate.d/apache2
sudo chmod -R 777 /var/log/apache2/
sudo chown -R ubuntu:ubuntu /var/log/apache2/

# Make ubuntu execute apache
sudo sed -i "s/export APACHE_RUN_USER=.*/export APACHE_RUN_USER=ubuntu/" /etc/apache2/envvars
sudo chown -R ubuntu /var/lock/apache2

sudo service apache2 restart

echo 'Install XDebug'

sudo apt-get install -y php-xdebug  &> /dev/null

echo "xdebug.default_enable=1" | sudo tee -a /etc/php/7.0/apache2/conf.d/20-xdebug.ini
echo "xdebug.idekey=\"vagrant\"" | sudo tee -a /etc/php/7.0/apache2/conf.d/20-xdebug.ini
echo "xdebug.remote_enable=1" | sudo tee -a /etc/php/7.0/apache2/conf.d/20-xdebug.ini
echo "xdebug.remote_autostart=1" | sudo tee -a /etc/php/7.0/apache2/conf.d/20-xdebug.ini
echo "xdebug.remote_connect_back=1" | sudo tee -a /etc/php/7.0/apache2/conf.d/20-xdebug.ini
echo "xdebug.remote_port=9000" | sudo tee -a /etc/php/7.0/apache2/conf.d/20-xdebug.ini
echo "xdebug.remote_handler=dbgp" | sudo tee -a /etc/php/7.0/apache2/conf.d/20-xdebug.ini
echo "xdebug.remote_host=10.0.2.2 " | sudo tee -a /etc/php/7.0/apache2/conf.d/20-xdebug.ini

echo "xdebug.remote_autostart=1" | sudo tee -a /etc/php/7.0/mods-available/xdebug.ini
echo "xdebug.remote_connect_back=1" | sudo tee -a /etc/php/7.0/mods-available/xdebug.ini

# Avoid PHP7 warinings
sudo sed -i "s/^error_reporting = E_ALL/error_reporting = ~E_DEPRECATED \& E_ALL/" /etc/php/7.0/apache2/php.ini
