# cs5031p3code

## Getting started Web-UI

## Build Setup

```bash

$ cd spa

# install dependencies
$ npm install

# serve with hot reload at localhost:3000
$ npm run dev

# build for production and launch server
$ npm run build
$ npm run start

# generate static project
$ npm run generate
```
### How to use SPA

Basically, please this run the SPA on the http://localhost:3000/ Port.  

### How to deploy Web-UI to lab's server

If you want to use the SPA on the specific server, the deployment flow is as follows.

```
cd spa
npm run generate
```
Then, dist directory will be generated.
Upload all files under a dist directory to /cs/home/$USER/nginx_default (must not include dist directory).
Then, we can access Web-UI from the link below.

```
https://$USER.host.cs.st-andrews.ac.uk/
```

The SPA requires a proxy setting to connect server API.  
As Nuxtjs and axios are used in this SPA, the server connection from the SPA will be blocked by Cross-Origin Resource Sharing Problem (CORS).  
You need to modify some codes in 'nuxt.config.js' to avoid CORS between SPA and server.
Please modify some codes in 'nux.config.js' based on your server host and port number.

```
nuxt.config.js: L:61-75
Please fix target URL based on the server you use.

proxy: {
    '/user': 'http://localhost:8080',
    '/users': 'http://localhost:8080',
    '/user/update': 'http://localhost:8080',
    '/admin': 'http://localhost:8080',
    '/page': 'http://localhost:8080',
    '/page/admin': 'http://localhost:8080',
    '/page/admin/message': 'http://localhost:8080',
    '/page/info': 'http://localhost:8080',
    '/page/content': 'http://localhost:8080',
    '/page/access': 'http://localhost:8080',
    '/page/access/user': 'http://localhost:8080',
    '/page/access/my': 'http://localhost:8080',
    '/page/access/mywrite': 'http://localhost:8080',
    '/userLogin': 'http://localhost:8080',
    '/adminLogin': 'http://localhost:8080',
},
```
