var config = require("../config/settingsmail");

module.exports = {

    email: function (from, to, text, subject, cb) {
        var helper = require("sendgrid").mail;
        var from_email = new helper.Email(from);
        var to_email = new helper.Email(to);
        var subject = subject;
        var content = new helper.Content("text/html", text);
        var mail = new helper.Mail(from_email, subject, to_email, content);

        var sg = require("sendgrid")(config.sendgrid);
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

    forgottenpassword: function (to, pass, cb) {
        module.exports.email(config.from, to, "<b>forgotten password ✔</b> " + pass, "SAM forgotten password", cb);
    },

	sendContactForm: function (form, cb) {
        module.exports.email(form.email, config.admin, form.message, "SAM new Contact Us Form", cb);
    },
}
