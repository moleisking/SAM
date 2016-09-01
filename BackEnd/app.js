var express = require('express');
var path = require('path');
var logger = require('morgan');
var cookieParser = require('cookie-parser');
var bodyParser = require('body-parser');
var passport = require('passport');

var home = require('./routes/home');
var web = require('./routes/web');
var users = require('./routes/user');
var auth = require('./routes/auth');
var work = require('./routes/work');

var app = express();

var allowCrossDomain = function (req, res, next) {
  var allowedOrigins = ['http://localhost:3000', 'http://localhost:63592', 'http://minitrabajo.me:3000']; 
  var origin = req.headers.origin;
  if (allowedOrigins.indexOf(origin) > -1)
    res.setHeader('Access-Control-Allow-Origin', origin);
  // res.header('Access-Control-Allow-Origin', 'http://localhost:3000');
  res.header('Access-Control-Allow-Methods', 'GET,PUT,POST,DELETE');
  res.header('Access-Control-Allow-Headers', 'X-Requested-With, Content-Type, authorization');
  // res.header('Access-Control-Allow-Credentials', true);
  next();
};

app.use(logger('dev'));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

app.use(passport.initialize());
app.use(allowCrossDomain);

app.use('/', home);
app.use('/', web);
app.use('/users', users);
app.use('/', auth);
// app.use('/works', work);

// catch 404 and forward to error handler
app.use(function (req, res, next) {
  var err = new Error('Not Found');
  err.status = 404;
  next(err);
});

// error handlers

// development error handler
// will print stacktrace
if (app.get('env') === 'development') {
  app.use(function (err, req, res, next) {
    res.status(err.status || 500);
    res.json({
      message: err.message,
      error: err
    });
  });
}

// production error handler
// no stacktraces leaked to user
app.use(function (err, req, res, next) {
  res.status(err.status || 500);
  res.json({
    message: err.message,
    error: {}
  });
});

module.exports = app;