var config = require("../config/settingsmail");

module.exports = {

    email: function (fromText, from, to, text, subject, cb) {
        var helper = require("sendgrid").mail;
        var from_email = new helper.Email(from, fromText);
        var to_email = new helper.Email(to);
        var content = new helper.Content("text/html", text);
        var mail = new helper.Mail(from_email, subject, to_email, content);

        var sg = require("sendgrid")(config.sendgrid);
        var request = sg.emptyRequest({
            method: "POST",
            path: "/v3/mail/send",
            body: mail.toJSON(),
        });

        sg.API(request, function (err, response) {
            console.log(response.statusCode);
            console.log(response.body);
            console.log(response.headers);
            return cb(err, response.statusCode, response.body, response.headers);
        });
    },

    forgottenpassword: function (to, pass, cb) {
        module.exports.email("SAM forgotten password", config.from, to, "<b>forgotten password ✔</b> " + pass, "SAM forgotten password", cb);
    },

    sendContactForm: function (form, cb) {
        form.message = form.name + " " + form.surname + "<br />" + form.message;
        module.exports.email("SAM Contact From", form.email, config.admin, form.message, "SAM new Contact Us Form", cb);
    },

    newMessage: function (front, fromUrl, to, cb) {
        module.exports.email("SAM Messages", "sam@sam.com", to,
            "<b>New message ✔</b> <a href='" + front + "/messages/" + fromUrl + "'>Click here to see it</a>.",
            "You got a new message", cb);
    },
}
