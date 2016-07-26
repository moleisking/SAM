var config = require('../config/settingsmail');
var mailer = require('nodemailer');
var transporter = mailer.createTransport({
    host: config.host,
    // port: config.port,
    // secure: config.secure,
    auth: {
        user: config.mail,
        pass: config.password
    }
});

module.exports = {

    forgottenpassword: function (user, cb) {
        var mailOptions = {
            from: config.from, // sender address
            to: user.email, // list of receivers
            subject: 'SAM forgotten password', // Subject line
            // text: text //, // plaintext body
            html: '<b>forgotten password ✔</b>' // You can choose to send an HTML body instead
        };
        transporter.sendMail(mailOptions, function (error, info) {
            if (error) {
                console.log(error);
                return cb(error, null);
            } else {
                console.log('Message sent: ' + info.response);
                return cb(null, info.response);
            };
        });
    }

}