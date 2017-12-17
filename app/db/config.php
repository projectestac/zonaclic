<?php

// ** MySQL settings ** //
define('DB_HOST', 'localhost');
define('DB_NAME', 'jclicrepo');
define('DB_USER', 'clic');
define('DB_PASSWORD', 'clic');
define('DB_CHARSET', 'utf8');
define('DB_COLLATE', '');

// ** Path to repository projects ** //
define('PROJECTS_PATH', '../../projects');
define('PROJECTS_INDEX', 'projects.json');

// ** Languages currently used in this repository ** //
define('LANGS', ['ca', 'es', 'en']);

// ** Possible MySQL full text search methods ** //
define('FTS_METHODS', ['natural', 'boolean']);

// ** Default disk quota for unlisted users is 50 MB ** //
define('DEFAULT_QUOTA', 52428800);

// ** Path to users root ** //
define('USERS_ROOT', '../../web/users');
define('SETTINGS_FILE', 'settings.json');


