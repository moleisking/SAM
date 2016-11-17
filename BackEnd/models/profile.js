var model = require('nodejs-model');

var profile = new model("Profile")
    .attr('id')    
    .attr('username', {
        validations: {
            presence: {
                message: 'Username is required!'
            }
        }
    })
    .attr('name', {
        validations: {
            presence: {
                message: 'Name is required!'
            }
        }
    })
    .attr('url', {
        validations: {
            presence: {
                message: 'Name Url is required!'
            }
        }
    })
    .attr('description')
    .attr('birthday')
    .attr('rating')
    .attr('address', {
        validations: {
            presence: {
                message: 'Address is required!'
            }
        }
    })
    .attr('email')
    .attr('hourRate')
    .attr('dayRate')
    .attr('regLng', {
        validations: {
            presence: {
                message: 'Latitude is required!'
            }
        }
    })
    .attr('regLat', {
        validations: {
            presence: {
                message: 'Longitude is required!'
            }
        }
    })
    .attr('curLat', {
        validations: {
            presence: {
                message: 'Current Latitude is required!'
            }
        }
    })
    .attr('curLng', {
        validations: {
            presence: {
                message: 'Current Longitude is required!'
            }
        }
    })
    .attr('mobile', {
        validations: {
            presence: {
                message: 'Mobile is required!'
            }
        }
    })
    .attr('credit')
    .attr('available')
    .attr('category', {
        validations: {
            presence: {
                message: 'Category is required!'
            }
        }
    })
    .attr('tags')
    .attr('activated');

module.exports = profile;
