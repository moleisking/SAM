var model = require('nodejs-model');

var profile = new model("Profile") 
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
    .attr('address', {
        validations: {
            presence: {
                message: 'Address is required!'
            }
        }
    })
    .attr('email')
    .attr('image')
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
    .attr('active')
    .attr('rating')
    .attr('category', {
        validations: {
            presence: {
                message: 'Category is required!'
            }
        }
    })
    .attr('birthday')
    .attr('tags')   
    .attr('authenticated')
    .attr('timeStamp');

     /*
     .attr('username', {
        validations: {
            presence: {
                message: 'Username is required!'
            }
        }
    })
     .attr('looking')
     .attr('activated');
     .attr('surname', {
        validations: {
            presence: {
                message: 'Surname is required!'
            }
        }
    })*/
    /*.attr('nameurl', {
        validations: {
            presence: {
                message: 'Name Url is required!'
            }
        }
    })*/

module.exports = profile;
