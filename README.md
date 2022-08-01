# Web Mini Games

Collection of __Mini Games__ in a Web-Browser.

__System Requirements:__ [NodeJS](https://nodejs.org/en/download/)

## Client: Web UI

User Interface for several __Mini Games__ developing with __Vue 3 in Vite__.

### Run Project

1. `cd .\web-ui\`
2. `npm install` to install dependencies from [package.json](web-ui/package.json)
3. `npm run dev` to compile and hot-reload for development ([Vite Configuration Reference](https://vitejs.dev/config/))
4. `npm run lint` to Lint with [ESLint](https://eslint.org/)
5. `npm run test:unit` to run unit tests with [Vitest](https://vitest.dev/)
6. `npm run build` for type-check, compile and minify for production

### Project Creation Commands

1. `npm install -g @vue/cli` (v5.0.8)
2. `npm init vue@latest` (v3.3.1) with __Typescript__, __Routing__, __Pinia__ State Management, __Vitest__ Unit Testing,
   __ESLint__ Code Quality and __Prettier__ Code Formatting
3. `cd ./web-ui`
4. `npm install` to install dependencies from [package.json](web-ui/package.json)
5. `npm install socket.io --save` for [Socket.IO](https://socket.io/get-started/)
6. `npm install element-plus --save` for [Element Plus Materials](https://element-plus.org/en-US/component/button.html)

## Server: Block Game

### Run Project

1. `cd ./server-block-game`
2. `npm run start`

### Project Creation Commands

1. `cd ./server-block-game`
2. `npm init -y` to init [package.json](server-block-game/package.json)
3. `npm install express socket.io --save` for [Socket.IO](https://socket.io/get-started/)
4. `npm install nodemon --save-dev` for hot-reload
