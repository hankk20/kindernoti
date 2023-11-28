## Config Server Enviroment
민감정보는 Git Public Repository에 공개 하지 않기 위해 .env 파일과 로컬에서 실행 하기위한 정보인 application-local.yml파일은 git으로 관리 하지 않는다. 
### Local
-  application-local.yml로 관리
    ```yaml
    encrypt:
      key: encryptkey
    spring:
      security:
        user:
          name: configuser
          password: configuserpassword
    ```
### Container
- .env 파일로 관리
    ```dotenv
    CONFIG_SERVER_ENCRYPT_KEY=key
    CONFIG_SERVER_USER=configuser
    CONFIG_SERVER_PASSWORD=configuserpassword
    ```