# 🎓 Edunext - Microservices E-Learning Platform

## 📁 Structure du projet

- **exam-service** : Gestion des examens et questions
- **eureka** : Service de découverte Eureka
- **api-gateway** : Gateway pour centraliser les appels d’API

## 🚀 Technologies utilisées

- Java 17 / Spring Boot
- Spring Cloud (Eureka, Gateway)
- Angular 16 (frontend)
- MySQL / JPA
- Maven
- Git / GitHub

## 🧪 Fonctionnalités principales

- ✅ CRUD des examens et questions
- ✅ Soumission d’un examen et calcul de score
- ✅ Téléchargement PDF des examens
- ✅ Recherche et filtrage
- ✅ Microservices avec Eureka et API Gateway

## 📖 Tester l'API avec Postman

### 🌐 Points de terminaison API disponibles

Voici quelques-uns des points de terminaison principaux que tu peux tester avec **Postman** :

1. **GET** `http://localhost:8093/api/exams`
   - **Description** : Récupère tous les examens.
   - **Réponse attendue** : Liste des examens.

2. **POST** `http://localhost:8093/api/exams`
   - **Description** : Crée un nouvel examen.
   - **Exemple de body** :
     ```json
     {
       "examTitle": "Test Java",
       "examDescription": "Un test pour vérifier les connaissances en Java",
       "examDuration": 60,
       "totalMarks": 100,
       "passingScore": 60,
       "scheduledDate": "2025-04-20",
       "expirationDate": "2025-05-20"
     }
     ```

3. **POST** `http://localhost:8093/api/exams/submit`
   - **Description** : Soumet un examen et calcule le score.
   - **Exemple de body** :
     ```json
     {
       "examId": 1,
       "answers": {
         "1": "Paris",
         "2": "Berlin"
       }
     }
     ```

### ⚙️ Comment tester avec Postman

1. **Lancer l'API** : Démarre ton API en utilisant Spring Boot sur le port 8093 (port de apiGateway).
   
2. **Configurer Postman** :
   - Ouvre **Postman**.
   - Ajoute une nouvelle requête HTTP (GET, POST, etc.) selon le point de terminaison que tu veux tester.
   - Pour une requête **POST**, colle le corps de la requête (exemple JSON ci-dessus) dans l'onglet **Body**.
   
3. **Exécuter les requêtes** : Clique sur "Send" dans Postman pour envoyer la requête. Tu verras les résultats dans la section de réponse de Postman, y compris les codes de statut HTTP, les en-têtes, et le corps de la réponse.

## 📝 Auteur

Bensalem Imen  
GitHub: [@bensalemimen](https://github.com/bensalemimen)
