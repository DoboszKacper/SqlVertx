FROM mysql:latest
ENV MYSQL_ROOT_PASSWORD 123
ENV MYSQL_DATABASE users
ENV MYSQL_USER kacper
ENV MYSQL_PASSWORD 123
ADD database.sql /docker-entrypoint-initdb.d
EXPOSE 3306

