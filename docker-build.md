To build and push docker image

1. mvn clean package -D skipTests
2. docker build -t expenses .
3. docker tag <IMAGE_ID> rrromix/expenses:<TAG>
4. docker push rrromix/expenses:<TAG>