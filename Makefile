all: docker

clean:
	mvn clean

compile: clean
	mvn package

docker: compile
	docker build -t dapidi/fakesmtp:latest .

