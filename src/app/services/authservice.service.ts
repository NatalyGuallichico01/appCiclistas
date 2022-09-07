import { Injectable } from '@angular/core';
import { AngularFireAuth} from '@angular/fire/compat/auth';
import { AngularFirestore } from '@angular/fire/compat/firestore';
import { Router } from '@angular/router';
import { User } from '../schema';

@Injectable({
  providedIn: 'root'
})
export class AuthserviceService {

  constructor(
    private fireauth: AngularFireAuth, 
    private router: Router, 
    private firestore: AngularFirestore,
  ) { }


  getObject( id: string, path: string){
    return this.firestore
    .collection(path)
    .doc(id)
    .valueChanges()
  }
  login(user: any) {
    return this.fireauth.signInWithEmailAndPassword(user.mail,user.password);
  }
  register(user: User) {
    return this.fireauth.createUserWithEmailAndPassword(user.mail, user.password);
  }
  createUser(user: User, path: string){
    console.log("datos usuario: ", user)
    this.firestore.collection(path)
    .doc(user.id)
    .set({id: user.id,
      mail: user.mail,
      name: user.name,
      long: user.long,
      lati: user.lati,
    });
  }
  //Método para crear la geololalizacion
  createGeo(){
    if(localStorage.getItem("longString")){
    const path = "geolocation";
    const id = this.firestore.createId();
    localStorage.setItem("geoID", id);
    this.firestore.collection(path)
    .doc(id)
    .set({
      id: id,
      uid : localStorage.getItem("idUser"),
      name : localStorage.getItem("nameUser"),
      longitude : localStorage.getItem("longString"),
      latitude : localStorage.getItem("latiString"),
    })
    }
    else{
      alert("error al crear geolocation")
    }
  }
    //Método para actualizar la geololalizacion
  updateGeo(){
    console.log("Actualización de geolocalización");
    console.log("Longitud: ",localStorage.getItem("longString"));
    console.log("Latitud: ",localStorage.getItem("latiString"));
    const path = "users";
    return this.firestore.collection(path)
      .doc(localStorage.getItem("idUser")) 
      .update({
        long : localStorage.getItem("longString"),
        lati : localStorage.getItem("latiString"),
    })
  }
  
  //Método para Salir 
  logout(){
    this.fireauth.signOut().then(()=>{
      localStorage.clear();
      this.router.navigate(['/login']);
    }, err=>{
      alert(err.message);
    })
  }
  
  //Método para reestablecer contraseña
  forgotPassword(email : string) {
    this.fireauth.sendPasswordResetEmail(email).then(() => {
      alert("Revisa tu correo");
      
    }, err => {
      console.log("error: ", err)
      alert('Something went wrong');
    })
  }
   //Método para traer los datos de geolocalizacion 
  getlocation(): Promise<any>{
    return new Promise ((resolve, reject) =>{
      navigator.geolocation.getCurrentPosition(resp=>{
        resolve ({long: resp.coords.longitude, lati: resp.coords.latitude})
        
      })

    })
  }

  showUsers(){
    const path =  "users";
    return this.firestore.collection(path)
    .snapshotChanges();
  }
  sendEmailForVarification(user : any) {
    console.log(user);
    user.sendEmailVerification().then((res : any) => {
      this.router.navigate(['/verify']);
    }, (err : any) => {
      alert('Something went wrong. Not able to send mail to your email.')
    })
  }

}
