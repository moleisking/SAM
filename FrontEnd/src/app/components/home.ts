import { Component, OnInit } from "@angular/core";

import { CategoriesService } from "../services/categories";
import { CategoryModel } from "../models/category";
import { UserModel } from "../models/user";
import { UserService } from "../services/user";

declare let google: any;

@Component({
    selector: "home-component",
    templateUrl: "../../views/home.html",
    styleUrls: ["../../styles/app-root.css"]
})

export class Home implements OnInit {

    private users: Array<UserModel>;
    private cats: Array<CategoryModel>;
    private message: string;

    private regLat: number;
    private regLng: number;
    private category: number;
    private radius: number = 5;

    constructor(private cat: CategoriesService, private user: UserService) { }

    getAddress(place: Object) {
        let address = place["formatted_address"];
        let location = place["geometry"]["location"];
        this.regLat = location.lat();
        this.regLng = location.lng();
        console.log("place", address, location, this.regLat, this.regLng);
    }

    getCategories() {
        this.cat.all().subscribe(
            c => {
                this.cats = c;
                this.category = c[0].id;
            },
            error => this.message = <any>error
        );
    }

    onChangeCategory(value: any) {
        this.category = value;
    }

    onChangeRadius(value: any) {
        this.radius = value;
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
            this.regLat = place.geometry.location.lat();
            this.regLng = place.geometry.location.lng();
            let address = place.formatted_address;
            this.getAddress(place);
        });

        this.getCategories();
    }

    search() {
        if (!this.regLat || !this.regLng)
            this.message = "Please, select a city.";
        else {
            this.user.search(this.regLat, this.regLng, this.category, this.radius).subscribe(
                (users) => {
                    console.log(users);
                    this.users = users;
                    if (users.length === 0)
                        this.message = "No users found.";
                    else
                        this.message = "";
                },
                error => this.message = <any>error,
                () => console.log("Done search call.")
            );
        }
    }
}
