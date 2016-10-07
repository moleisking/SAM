import { Injectable } from "@angular/core";
import { Http, Headers, Response, RequestOptions } from "@angular/http";
import { Settings } from "../config/settings";
import { UserModel } from "../models/user";
import { ProfileModel } from "../models/profile";
import { Observable } from "rxjs/Rx";

@Injectable()

export class UserService {

  constructor(private http: Http) { }

  register(user: UserModel, regLat: any, regLng: any): Observable<any> {
    let body = "name=" + user.name + "&pass=" + user.pass + "&email=" + user.email
      + "&regLat=" + regLat + "&regLng=" + regLng
      + "&category=" + user.category + "&tags=" + user.tags
      + "&address=" + user.address + "&mobile=" + user.mobile;
    let headers = new Headers();
    headers.append("Content-Type", "application/x-www-form-urlencoded");
    let options = new RequestOptions({ headers: headers });

    return this.http.post(Settings.backend_url + "/signup", body, options).catch(this.handleError);
  }

  saveProfile(profileform: ProfileModel, image: string): Observable<any> {
    let headers = new Headers();
    headers.append("authorization", "JWT " + localStorage.getItem("auth_key"));
    headers.append("Content-Type", "application/x-www-form-urlencoded");
    let c = "description=" + profileform.description + "&address=" + profileform.address
      + "&mobile=" + profileform.mobile + "&image=" + image + "&dayRate=" + profileform.dayRate
      + "&hourRate=" + profileform.hourRate + "&curLat=" + profileform.curLat + "&curLng=" + profileform.curLng
      + "&category=" + profileform.category + "&tags=" + profileform.tags;

    return this.http.post(Settings.backend_url + "/users/saveprofile", c, { headers: headers }).catch(this.handleError);
  }

  all(): Observable<UserModel[]> {
    let headers = new Headers();
    headers.append("authorization", "JWT " + localStorage.getItem("auth_key"));
    let options = new RequestOptions({ headers: headers });

    return this.http.get(Settings.backend_url + "/users", options)
      .map((res: Response) => res.json().all).catch(this.handleError);
  }

  getMyProfile(): Observable<ProfileModel> {
    let headers = new Headers();
    headers.append("authorization", "JWT " + localStorage.getItem("auth_key"));
    let options = new RequestOptions({ headers: headers });

    return this.http.get(Settings.backend_url + "/users/getmyprofile", options)
      .map((res: Response) => res.json().myprofile).catch(this.handleError);
  }

  getProfile(id: string): Observable<ProfileModel> {
    return this.http.get(Settings.backend_url + "/users/getprofile/" + id)
      .map((res: Response) => res.json().profile).catch(this.handleError);
  }

  forgottenpassword(form: any): Observable<any> {
    let creds = "email=" + form.email;
    let headers = new Headers();
    headers.append("Content-Type", "application/x-www-form-urlencoded");
    let options = new RequestOptions({ headers: headers });

    return this.http.post(Settings.backend_url + "/forgottenpassword", creds, options)
      .map(this.extractData).catch(this.handleError);
  }

  search(regLat: number, regLng: number, category: number, radius: number): Observable<UserModel[]> {
    let body = "regLat=" + regLat + "&regLng=" + regLng + "&category=" + category + "&radius=" + radius;
    let headers = new Headers();
    headers.append("Content-Type", "application/x-www-form-urlencoded");
    let options = new RequestOptions({ headers: headers });

    return this.http.post(Settings.backend_url + "/users/search", body, options)
      .map((res: Response) => res.json().search).catch(this.handleError);
  }

  private extractData(res: Response) {
    // console.log(res);
    let body = res.json();
    // console.log(body.data);
    return body.data || {};
  }

  private handleError(error: any) {
    // console.error(error);
    let errMsg = (error.message) ? error.message :
      (error._body) ? error._body :
        error.status ? `${error.status} - ${error.statusText}` : "Server error";
    console.error(errMsg);
    return Observable.throw(errMsg);
  }
}
