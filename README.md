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

### How to deploy Web-UI to lab's server

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

You need to fix codes to avoid CORS problrem between SPA and server.
Please fix nux.config.js based on your server host and port number.
```
nuxt.config.js: L:53-60
Please fix target URL based on the server you use.

proxy: {
    '/user/': 'http://localhost:8080',
    '/users/': 'http://localhost:8080',
    '/admin/': 'http://localhost:8080',
    '/page/': 'http://localhost:8080',
    '/userLogin/': 'http://localhost:8080',
    '/adminLogin/': 'http://localhost:8080',
},
```
