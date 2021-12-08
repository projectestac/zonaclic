## Miscellaneous services for [clicZone](https://clic.xtec.cat)

The site [clic.xtec.cat](http://clic.xtec.cat) hosts a big repository of open educational activities created by teachers of different countries using the open source authoring tool [JClic](https://projectestac.github.io/jclic).

The [main repository of activities](https://clic.xtec.cat/repo) is accessible through a web app build with [Polymer](https://www.polymer-project.org/). Check the [jclic-repo](https://github.com/projectestac/jclic-repo) project on GitHub for more information about this web app.

The clicZone project is organized into four main directories:

- `/web`: Contains several static files served at root level on the web site.

  - `/web/dist/jnlp`: Several JNLP (_Java Launch Network Protocol_) files used to deliver to the final user the main components of JClic (Player, Author and Reports). The content of some of this files are dynamically updated depending on the query parameters passed to `/app/jnlp` (see below).
  - `/web/repo`: Here is where the web app used to access the main repository of activities is deployed. See [jclic-repo](https://github.com/projectestac/jclic-repo) for details.
  - `/web/projects`: Root directory of the projects repository. Currently, it contains only some samples used to test the system.
  - `/web/mylib`: This directory contains a web app that allows authorized users to manage their own library of JClic activities. It makes use of the services implemented in `/app/db/userlib` (see below).
  - `/web/users`: This directory will be filled with JClic projects published by authorized users. Currently, it contains only a few examples.

- `/mysql`: Contains the SQL scripts used to create the [MySQL database](https://dev.mysql.com/) needed by the app. MySQL version 5.7 or later is needed.

- `/app`: This directory contains the implementation of the services. PHP version 7.0 or later is needed.

  - `/app/db`: The file `index.php` redirects old clicZone project paths to its URL on the new repository.
  - `/app/db/userlib`: Contains several scripts used to validate users (`getUserInfo.php`) , to upload or update new projects (`uploaduserFile.php`) to its personal space, and to delete obsolete activities (`deleteProject.php`).
  - `/app/db/repo-search`: Performs full-text searches on the main repository of activities. This service is called by [jclic-repo](https://github.com/projectestac/jclic-repo) when the user searches for activities with specific words on its description or tag fields.
  - `/app/jnlp`: Dynamically updates the content of some JNLP files based on the query string passed. This is needed by the _JClic Installer_ Java app, and also to dynamically launch the activities using _JClic Player_.
  - `/app/redirect`: This PHP script will redirect old clicZone static pages to its new URL.

- `/vm`: Scripts used to build a [Vagrant](https://www.vagrantup.com/) virtual machine used only in development environments. Check [vm/README.md](https://github.com/projectestac/zonaclic/blob/master/vm/README.md) for details.

- `/devilbox`: Configuration files for the [Devilbox](http://devilbox.org/) LAMP stack, used only in development.

- `/docker`: Configuration files for an ecosystem of docker images orquested by `Docker-compose`, used in development.
