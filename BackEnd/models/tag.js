var model = require('nodejs-model');
var bCrypt = require('bcrypt-nodejs');

var tag = new model("tag")
    .attr('id', {
        validations: {
            presence: {
                message: 'Id is required!'
            }
        }
    })
    .attr('en', {
        validations: {
            presence: {
                message: 'Name is required!'
            }
        }
    })
    .attr('es', {
        validations: {
            presence: {
                message: '¡El nombre es obligatorio!'
            }
        }
    })
    .attr('de', {
        validations: {
            presence: {
                message: 'Name ist erforderlich!'
            }
        }
    })
    .attr('it', {
        validations: {
            presence: {
                message: 'è richiesto nome!'
            }
        }
    })
    .attr('description');

module.exports = category;