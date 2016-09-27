module.exports = {

    CalcDist: function (coords1, coords2) {
        var m = module.exports;
        var R = 6371; // km
        var dLat = m.toRad(parseFloat(coords2.lat) - parseFloat(coords1.currentLat));
        var dLon = m.toRad(parseFloat(coords2.lng) - parseFloat(coords1.currentLng));
        var lat1 = m.toRad(parseFloat(coords1.currentLat));
        var lat2 = m.toRad(parseFloat(coords2.lat));

        var a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
        var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        var d = R * c;
        return d;
    },

    toRad: function (value) {
        return value * Math.PI / 180;
    }

}
