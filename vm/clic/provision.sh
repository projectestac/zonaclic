#!/bin/bash

source "/vm/provision/functions.sh"

echo 'Provision clic zone'

sqldir=/dades/mysql

create_mysql_db "jclicrepo"
cat $sqldir/jclicrepo.sql | sudo mysql -uroot -p$pass jclicrepo
cat $sqldir/user.sql | sudo mysql -uroot -p$pass jclicrepo
