import { Component, OnInit } from "@angular/core";

declare let google: any;

@Component({
    selector: "home-component",
    templateUrl: "/views/home.html"
})

export class Home implements OnInit {

    private message: string;

    getAddress(place: Object) {
        let address = place["formatted_address"];
        let location = place["geometry"]["location"];
        let lat = location.lat();
        let lng = location.lng();
        console.log("Object", address, location, lat, lng);
    }

ngOnInit() {
    let searchBox: any = document.getElementById("location");
    let options = {
      types: [
        // return only geocoding results, rather than business results.
        "geocode",
      ],
    //   componentRestrictions: { country: "my" }
    };
    let autocomplete = new google.maps.places.Autocomplete(searchBox, options);

    // Add listener to the place changed event
    autocomplete.addListener("place_changed", () => {
      let place = autocomplete.getPlace();
      let lat = place.geometry.location.lat();
      let lng = place.geometry.location.lng();
      let address = place.formatted_address;
      this.getAddress(place);
    });
  }
}