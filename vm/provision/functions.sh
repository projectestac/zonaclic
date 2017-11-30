#!/bin/bash

pass=clic

function mysql_import_db {
    dbname=$1
    sql=$2

    echo "Importing DB  $dbname..."
    create_mysql_db $dbname
    cat $sql | sudo mysql -uroot -p$pass $dbname
    echo 'Done'
}

function mkdir_777 {
    folder=$1

    sudo mkdir -p $folder
    chown_777 $folder
}

function chown_777 {
    folder=$1

    sudo chown -R ubuntu:www-data $folder
    sudo chmod -R 777 $folder
}

function execute_in_mysql {
    sudo mysql -uroot -p$pass -e "$1"
}

function create_mysql_db {
    dbname=$1
    execute_in_mysql "CREATE DATABASE IF NOT EXISTS $dbname"
}

function load_version {
    if [ ! -f /vm_version ]; then
        #First version ever
        version=2015040100
        save_version $version
    else
        version=`cat /vm_version`
    fi
}


function save_version {
    echo 'Upgraded to '$1
    echo $1 | sudo tee /vm_version &> /dev/null
}


