#!/bin/bash

source "/vm/provision/functions.sh"

/vm/provision/base.sh

/vm/provision/php7.sh
/vm/provision/mysql.sh $pass
/vm/provision/squid.sh

/vm/clic/provision.sh

sudo service apache2 restart

# Here we must write the latest version number
save_version 2018040400
