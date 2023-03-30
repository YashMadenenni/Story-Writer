# cs5031p3code

#### Building and running the project:

Navigate to the root dir using
```
cd backend
```

Command to create package
```
mvn package
```

To run the tests:
```
mvn test
```

To run the code:
```
mvn spring-boot:run
```

#### Running GUI and CLI

###### GUI

In a new terminal execute these commands
```
cd spa
```

Install dependencies
```
npm install
```

Serve with hot reload at localhost:3000
```
npm run dev
```
The server will start running at localhost:3000.

Build for production and launch server
```
npm run build
npm run start
```

Generate static project
```
npm run generate
```

###### CLI
To run CLI user first have to fire up the server using command
```
mvn spring-boot:run
```
As soon as the server is started by running the command mvn spring-boot:run the CLI will get activated and it will start accepting inputs from the user.
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
nuxt.config.js: L:60-80
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
    '/page/access/my/gui': 'http://localhost:8080',
    '/page/access/mywrite/gui': 'http://localhost:8080',
    '/page/access/user/gui': 'http://localhost:8080',
    '/page/access/gui': 'http://localhost:8080',
    '/userLogin': 'http://localhost:8080',
    '/adminLogin': 'http://localhost:8080',
},
```
