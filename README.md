![Logo](https://github.com/zh-efimenko/Mailer/blob/logo/docs/logo.jpg?raw=true) 

<p align="center">
    <a href="https://github.com/zh-efimenko/Mailer/releases"><img alt="GitHub release" src="https://img.shields.io/github/release/zh-efimenko/Mailer.svg"></a>
    <a href="https://microbadger.com/images/eefimenko/mailer"><img alt="DockerHub Size" src="https://images.microbadger.com/badges/image/eefimenko/mailer.svg"></a>
    <a href="https://github.com/zh-efimenko/Mailer/blob/master/LICENSE.txt"><img alt="license" src="https://img.shields.io/github/license/zh-efimenko/Mailer.svg"></a>
</p>

# Mailer

## Running

### From sources

To build and run the application, you will need Java and Docker preinstalled.

```bash
$ git clone https://github.com/zh-efimenko/Mailer.git
$ cd Mailer
$ ./gradlew build
```
* Set up **_AUTHORIZATION_TOKEN_**, your gmail credentinals **_MAIL_USERNAME_**, **_MAIL_PASSWORD_**
```bash
$ docker-compose up -d
```


### Dockerimage

```bash
$ docker run -d --name mailer --restart always \
             -v $DATA_DIR:/data/ \
             -e AUTHORIZATION_TOKEN=$AUTHORIZATION_TOKEN \
             -e MAIL_USERNAME=$MAIL_USERNAME \
             -e MAIL_PASSWORD=$MAIL_PASSWORD \
             eefimenko/mailer
```


### Docker Compose

```yml
version: '3'

services:

  rabbitmq:
    image: rabbitmq:3-management
    restart: always
    ports:
      - 15672:15672
      - 5672:5672
      
  mailer:
    depends_on:
      - rabbitmq
    build:
      context: .
      dockerfile: /docker/Dockerfile
    image: eefimenko/mailer
    environment:
      AUTHORIZATION_TOKEN: '&d5yNc6FkoB0'
      MAIL_USERNAME: 'username@gmail.com'
      MAIL_PASSWORD: 'password'
      RABBITMQ_ENABLE: 'true'
      RABBITMQ_HOST: rabbitmq
    ports:
      - 8080:8080
    restart: always
    volumes:
      - ./data:/data

```


## Prerequisites

The Application uses several `environment variables`. 

In order to send attachments with message to need to encode them to `base64`.

Application can set up with `any email provider`: Google, Yandex, others.


### Required environment variables

* `AUTHORIZATION_TOKEN`

Set the **AUTHORIZATION_TOKEN** lets the application to access to your application. The put **AUTHORIZATION_TOKEN** to 
HTTP header is **_Autorization_**.

* `MAIL_USERNAME` & `MAIL_PASSWORD`

Set the **MAIL_USERNAME** and **MAIL_PASSWORD** environment variable with the email and password for your
email provider.


### Optional environment variables

Next variables can be changed if needed.  


#### Email provider variables

* `MAIL_HOST`

Set the **MAIL_HOST** environment variable for override default: **_smtp.gmail.com_**

* `MAIL_PORT`

Set the **MAIL_PORT** environment variable for override default: **_587_**

* `MAIL_PROTOCOL`

Set the **MAIL_PROTOCOL** environment variable for override default: **_smtp_**

* `MAIL_DEFAULT_ENCODING`

Set the **MAIL_DEFAULT_ENCODING** environment variable for override default: **_UTF-8_**

* `MAIL_PROPERTIES`

Set the **MAIL_PROPERTIES** environment variable for override default: **_mail.smtp.starttls.enable=true,mail.smtp.starttls.required=true_**


#### RabbitMQ variables

* `RABBITMQ_ENABLE`

Set the **RABBITMQ_ENABLE** environment variable for enable RabbitMQ, default: **_false_**

* `RABBITMQ_EXCHANGER`

Set the **RABBITMQ_EXCHANGER** environment variable to create exchanger, default: **_mailer_**

* `RABBITMQ_QUEUE`

Set the **RABBITMQ_QUEUE** environment variable to create queue, default: **_mailer_**

* `RABBITMQ_ROUTING_KEY`

Set the **RABBITMQ_ROUTING_KEY** environment variable to create routing-key, default: **_mailer-key_**

* `RABBITMQ_HOST`

Set the **RABBITMQ_HOST** environment variable for override default: **_localhost_**

* `RABBITMQ_PORT`

Set the **RABBITMQ_PORT** environment variable for override default: **_5672_**

* `RABBITMQ_DEFAULT_VHOST`

Set the **RABBITMQ_DEFAULT_VHOST** environment variable for override default: **_/_**

* `RABBITMQ_DEFAULT_USER`

Set the **RABBITMQ_DEFAULT_USER** environment variable for override default: **_guest_**

* `RABBITMQ_DEFAULT_PASS`

Set the **RABBITMQ_DEFAULT_PASS** environment variable for override default: **_guest_**


## Sending message by RabbitMQ

In order to send message you need to create the follow `json`:

```json
{
  "from": {
    "address": "from@mail.ru",
    "personal": "personal"
  },
  "to": [
    {
      "address": "to@mail.ru",
      "recipientType": "{TO|CC|BCC}"
    }
  ],
  "subject": "Subject",
  "message": "message",
  "type": "{mail-message}"
}
```

Response:

```json
{
  "status": "{OK|FAIL}"
}
```


## Sending message by template by RabbitMQ

In order to send message by template you need to create the follow `json`:

```json
{
  "from": {
    "address": "from@mail.ru",
    "personal": "personal"
  },
  "to": [
    {
      "address": "to@mail.ru",
      "recipientType": "{TO|CC|BCC}"
    }
  ],
  "subject": "Subject",
  "template": {
    "namespace": "Namespace",
    "name": "template.ftl",
    "content": {
      "name": "name",
      "link": "link"
    }
  },
  "type": "{mail-template}"
}
```

Response:

```json
{
  "status": "{OK|FAIL}"
}
```


## Sending template by RabbitMQ

In order to send template you need to create the follow `json`:

```json
{
  "namespace": "Namespace",
  "name": "confirmation.ftl",
  "body": "base64(body)",
  "type": "{template}"
}
```

Response:

```json
{
  "status": "{OK|FAIL}"
}
```


## Swagger

In order to enhance your opportunities in frontend and microservices development to use **_Swagger_**.

To get Mailer API follow the url:

`http://{host}/swagger-ui.html`
