import { Injectable } from "@angular/core";
import { Http, Headers, Response } from "@angular/http";
import { Settings } from "../config/settings";
import { Work } from "../models/work";
import { Observable } from "rxjs/Rx";

@Injectable()

export class WorkService {

  constructor(private http: Http) { }

  create(work) {
    let creds = "name=" + work.name + "&description=" + work.description + "&category=" + work.category
      + "&tags=" + work.tags;
    let headers = new Headers();
    headers.append("authorization", "JWT " + localStorage.getItem("auth_key"));
    headers.append("Content-Type", "application/X-www-form-urlencoded");

    return new Promise((resolve, reject) => {
      this.http.post(Settings.backend_url + "/work/create", creds, { headers: headers }).subscribe(
        (data) => {
          if (data.json().success)
            resolve(true);
          else {
            console.log(data.json().message);
            reject(data.json().message);
          }
        }
      )
    });
  }
}