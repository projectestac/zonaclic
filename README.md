## Miscellaneous services for [clicZone](https://clic.xtec.cat)

The site [clic.xtec.cat](http://clic.xtec.cat) hosts a big repository of interactive educational activities created and shared by teachers of different countries using the free authoring tool [JClic](https://projectestac.github.io/jclic).

The engine that pushes the [main repository of activities](https://clic.xtec.cat/repo) is a web app build with [Polymer](https://www.polymer-project.org/). Full [source code](https://github.com/projectestac/jclic-repo) of this app is also available on GitHub.

This project is organized into four main sections:

- `/web`: Contains several static files served at root level on the web site.

  - `/web/dist/jnlp`: Several JNLP (_Java Launch Network Protocol_) files used to deliver to the final user the main components of JClic (Player, Author and Reports). The content of some of this files is dynamically updated based on the query parameters passed to `/app/jnlp` (see below).
  - `/web/repo`: Web app used to query the main repository of projects. See https://clic.xtec.cat/repo for details.
  - `/web/projects`: Root directory of the projects repository, currently containing only samples used for testing.
  - `/web/mylib`: Static web app that allows users to upload to the server their own JClic projects. This app makes use of the services implemented in `/app/db/userlib` (see below).
  - `/web/users`: Sample user space folder. This directory will be filled with the JClic projects published by users.

- `/mysql`: Contains the SQL scripts used to create the [MySQL database](https://dev.mysql.com/) used in the services. MySQL version 5.7 or later is needed.

- `/app`: This directory contains several [PHP scripts](http://php.net/). PHP version 7.0 or later is needed.

  - `/app/db`: The file `index.php` redirects old clicZone project paths to its URL on the new repository.
  - `/app/db/userlib`: Contains several scripts used to validate users (`getUserInfo.php`) , to upload or update new projects (`uploaduserFile.php`) to its personal space, and to delete obsolete activities (`deleteProject.php`).
  - `/app/db/repo-search`: Performs full-text searches on the main repository of activities. This service is called by [jclic-repo](https://github.com/projectestac/jclic-repo) when the user performs a query based on words present on project descriptions.
  - `/app/jnlp`: Dynamically updates the content of some JNLP files based on the query string passed. This is needed by the Java app that makes possible to download and locally install the JClic projects, and also to dynamically launch the activities using the Java player.
  - `/app/redirect`: This PHP script will redirect old clicZone static pages to its new URL.

- `/vm`: Scripts used to build a [Vagrant](https://www.vagrantup.com/) virtual machine used only in development environments. Check `Vagrantfile` for details.




