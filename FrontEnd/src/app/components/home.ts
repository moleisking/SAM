import { Component, OnInit } from "@angular/core";
import { CategoriesService } from "../services/categories";
import { CategoryModel } from "../models/category";

declare let google: any;

@Component({
    selector: "home-component",
    templateUrl: "../../views/home.html",
    styleUrls: ["../../styles/app-root.css"]
})

export class Home implements OnInit {

    private cats: Array<CategoryModel>;
    private message: string;

    constructor(private cat: CategoriesService) { }

    getAddress(place: Object) {
        let address = place["formatted_address"];
        let location = place["geometry"]["location"];
        let lat = location.lat();
        let lng = location.lng();
        console.log("Object", address, location, lat, lng);
    }

    getCategories() {
        this.cat.all().subscribe(
            c => { this.cats = c; },
            error => this.message = <any>error
        );
    }

    onChangeCategory(value:any) {
        console.log(value);
    }

    ngOnInit() {
        let searchBox: any = document.getElementById("location");
        let options = {
            // return only geocoding results, rather than business results.
            types: ["geocode"],
            componentRestrictions: { country: "es" }
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

        this.getCategories();
    }
}