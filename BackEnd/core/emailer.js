var config = require("../config/settingsmail");
var mailer = require("nodemailer");
var smtpTransport = require("nodemailer-smtp-transport");
var transporter = mailer.createTransport(smtpTransport({
    service: config.service,
    auth: {
        user: config.mail,
        pass: config.password
    }
}));
// var transporter = mailer.createTransport({
//     host: config.host,
//     // port: config.port,
//     // secure: config.secure,
//     auth: {
//         user: config.mail,
//         pass: config.password
//     }
// });
var settings = require("../config/settingsmail");

module.exports = {

    email: function (from, to, text, cb) {
        var helper = require("sendgrid").mail;
        var from_email = new helper.Email(from);
        var to_email = new helper.Email(to);
        var subject = "Hello World from the SendGrid Node.js Library!";
        var content = new helper.Content("text/plain", text);
        var mail = new helper.Mail(from_email, subject, to_email, content);

        var sg = require("sendgrid")(settings.sendgrid);
        var request = sg.emptyRequest({
            method: "POST",
            path: "/v3/mail/send",
            body: mail.toJSON(),
        });

        sg.API(request, function (error, response) {
            console.log(response.statusCode);
            console.log(response.body);
            console.log(response.headers);
            return cb(null, response.statusCode, response.body, response.headers);
        });
    },

    forgottenpassword: function (user, cb) {
        var mailOptions = {
            from: config.from, // sender address
            to: user.email, // list of receivers
            subject: "SAM forgotten password", // Subject line
            // text: text //, // plaintext body
            html: "<b>forgotten password ✔</b>" // You can choose to send an HTML body instead
        };
        transporter.sendMail(mailOptions, function (error, info) {
            if (error) {
                console.log(error);
                return cb(error, null);
            } else {
                console.log("Message sent: " + info.response);
                return cb(null, info.response);
            };
        });
    },

	sendContactForm: function (form, cb) {
        var mailOptions = {
            from: form.email, // sender address
            to: config.admin, 
            subject: "SAM new Contact Us Form", // Subject line
            text: form.message //, // plaintext body
        };
        transporter.sendMail(mailOptions, function (error, info) {
            if (error) {
                console.log(error);
                return cb(error, null);
            } else {
                console.log("Message sent: " + info.response);
                return cb(null, info.response);
            };
        });
    }

}