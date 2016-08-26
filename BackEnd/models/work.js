var model = require('nodejs-model');

var work = new model("Work")
    .attr('name', {
        validations: {
            presence: {
                message: 'Work name is required!'
            }
        }
    })
    .attr('username', {
        validations: {
            presence: {
                message: 'Username is required!'
            }
        }
    })
    .attr('nameurl', {
        validations: {
            presence: {
                message: 'Name URL is required!'
            }
        }
    })
    .attr('description', {
        validations: {
            presence: {
                message: 'Work description is required!'
            }
        }
    })
    .attr('category', {
        validations: {
            presence: {
                message: 'Category is required!'
            }
        }
    })
    .attr('tags');

module.exports = work;