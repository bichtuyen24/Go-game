const express = require('express');
const app = express();
const port = 3000;

app.get('/', (req, res) => {
    return res.send('Home');
});
app.get('/game', (req, res) => {
    return res.send('Game');
});
app.get('/login', (req, res) => {
    return res.send('Login');
});
app.get('/register', (req, res) => {
    return res.send('Register');
});
app.listen(port, () => console.log('Example app listening at http://localhost:${port}!'));