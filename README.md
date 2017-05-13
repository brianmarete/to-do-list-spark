# To-Do List

This is a simple to do list application build using the Spark Java microframework.
Built with: Java 8 (Spark) and PostgreSQL.

## Setup

1. Clone the repository
``` shell
$ git clone https://github.com/brianmarete/to-do-list-spark.git
$ cd to-do-list-spark
```

2. Create the application database in the Postgres shell
``` shell
$ psql
CREATE DATABASE to_do;
\q
```

3. Load the database schema
``` shell
$ psql to_do < to_do.sql
```
4. Run the application
``` shell
$ ./gradlew run # or 'gradlew run' in Windows
```

# Author
Brian Marete

