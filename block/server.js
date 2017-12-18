'use strict';

const express = require('express');
const app = express();

const PORT = process.env.PORT;
const HOST = process.env.HOST;
// App
app.get('/', (req, res) => {
  res.send('Hello world\n');
});

app.listen(PORT, HOST);
console.log(`Running on http://${HOST}:${PORT}`);
