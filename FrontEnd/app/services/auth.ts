import { Injectable } from "@angular/core";
import { Http, Headers } from "@angular/http";
import { Settings } from "../config/settings";

@Injectable()

export class AuthService {

  constructor(private http: Http) { }

  login(usercreds) {
    let creds = "name=" + usercreds.name + "&pass=" + usercreds.pass;
    let headers = new Headers();
    headers.append("Content-Type", "application/X-www-form-urlencoded");

    return new Promise((resolve, reject) => {
      this.http.post(Settings.backend_url + "/api/authenticate", creds, { headers: headers }).subscribe(
        (data) => {
          if (data.json().success) {
            localStorage.setItem("auth_key_name", usercreds.name);
            localStorage.setItem("auth_key", data.json().token.split(" ")[1]);
            resolve(true);
          }
          else {
            console.log(data.json().message);
            reject(data.json().message);
          }
        }
      )
    });
  }

  logout() {
    localStorage.removeItem("auth_key");
    localStorage.removeItem("auth_key_name");
  }

  isLoggedIn() {
    if (localStorage.getItem("auth_key") !== null)
      return true;
  }
}