var model = require('nodejs-model');
var bCrypt = require('bcrypt-nodejs');

var transaction = new model("Product")
    .attr('userid', {
        validations: {
            presence: {
                message: 'Userid is required!'
            }
        }
    })
    .attr('image1')
    .attr('image2')
    .attr('image3')
    .attr('image4')
    .attr('title', {
        validations: {
            presence: {
                message: 'Title is required!'
            }
        }
    })
    .attr('description', {
        validations: {
            presence: {
                message: 'Description is required!'
            }
        }
    })
    .attr('price', {
        validations: {
            presence: {
                message: 'Value is required!'
            },
            length: {
                minimum: 1,
                maximum: 20,
                messages: {
                    tooShort: 'password is too short!',
                    tooLong: 'password is too long!'
                }
            }
        },        
    })
    .attr('datetimestamp', {
        validations: {
            presence: {
                message: 'DateTimeStamp is required!'
            }
        }
    });    

module.exports = product;