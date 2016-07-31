import { Injectable } from "@angular/core";
import { Http, Headers } from "@angular/http";
import { Settings } from "../config/settings";

@Injectable()

export class WebService {

  constructor(private http: Http) { }

  about() {
    return new Promise((resolve) => {
      this.http.get(Settings.backend_url + "/about").subscribe(
        (data) => {
          if (data.json().success)
            resolve(data.json().data);
          else {
            console.log(data.json().message);
            resolve(data.json().message);
          }
        }
      )
    });
  }

    termsConditions() {
    return new Promise((resolve) => {
      this.http.get(Settings.backend_url + "/termsconditions").subscribe(
        (data) => {
          if (data.json().success)
            resolve(data.json().data);
          else {
            console.log(data.json().message);
            resolve(data.json().message);
          }
        }
      )
    });
  }
}