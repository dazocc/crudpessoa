docker run -d -p 27017:27017 -p 28017:28017 -e AUTH=no tutum/mongodb
docker run -d --name crudpessoa -e POSTGRES_PASSWORD=docker -p 5432:5432  -e PGDATA=/var/lib/postgresql/data/pgdata -v /custom/mount:/var/lib/postgresql/data postgres

--docker file
FROM openjdk:11
EXPOSE 8080
ADD target/crud-pessoa.jar crud-pessoa.jar
ENTRYPOINT ["java","-jar", "/crud-pessoa.jar"]
--rodar comandos a seguir dentro da pasta do projeto
docker build -t crud-pessoa.jar .
docker run -p 8080:8080 crud-pessoa.jar
docker login
docker tag crud-pessoa.jar dazocc/crud-pessoa.jar
docker push dazocc/crud-pessoa.jar
