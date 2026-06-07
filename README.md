#  Mini-Projet Othello (Reversi)

Bienvenue dans le dépôt officiel du projet **Othello Game** ! Ce projet est une implémentation logicielle moderne et interactive du célèbre jeu de plateau Othello (également connu sous le nom de Reversi), développée en **Java** avec une interface graphique **JavaFX**.

L'application a été conçue en respectant rigoureusement le patron d'architecture **MVC (Modèle-Vue-Contrôleur)** pour garantir un code propre, modulaire et facilement évolutif.

---

##  Fonctionnalités Principales

* **Mode de Jeu Classique :** Affrontement fluide entre deux joueurs sur une grille de format standard ($8 \times 8$).
* **Intelligence Artificielle (IA) :** Possibilité de jouer seul face à l'ordinateur.
* **Historique des Coups :** Suivi textuel en temps réel de toutes les actions et captures effectuées durant la partie.
* **Sauvegarde des Scores :** Intégration d'une base de données locale pour enregistrer et suivre l'historique des scores et des victoires.
* **Interface Graphique Moderne :** Design soigné, fluide et responsive avec un panneau de configuration des paramètres de jeu avant le lancement.

---

##  Architecture du Projet (MVC)

Le code source est structuré de manière à séparer la logique métier des composants visuels :

* **Modèle (`com.othello.model`) :** Contient la logique pure du jeu (gestion de la matrice du plateau `Board`, règles de capture des pions, calcul des scores, sessions de jeu `GameSession` et accès aux données `DatabaseDAO`).
* **Vue (`com.othello.view` & `com.othello.ui`) :** Gère l'affichage des fenêtres, le rendu graphique des pions noirs et blancs sur le canvas JavaFX, ainsi que les menus de configuration.
* **Contrôleur (`com.othello.controller`) :** Intercepte les actions de l'utilisateur (clics de souris sur la grille), pilote le modèle et demande la mise à jour de la vue.

---

##  Technologies Utilisées

* **Langage :** Java
* **Interface Graphique :** JavaFX 20.0.1
* **Gestionnaire de Projet & Dépendances :** Maven
* **Base de Données :** SQLite (Base de données locale et légère embarquée)

---

##  Guide d'Installation et Exécution

Pour exécuter ce projet sur votre machine locale, suivez ces quelques étapes simples.

### Prerequisites
Assurez-vous d'avoir installé sur votre ordinateur :
* Le **JDK 17** (ou une version ultérieure).
* Un IDE comme **IntelliJ IDEA** (recommandé) ou Eclipse.

### Étapes à suivre :

1.  **Cloner le projet** sur votre machine :
    ```bash
    git clone [https://github.com/lizineb7/MiniProjetOthello.git](https://github.com/lizineb7/MiniProjetOthello.git)
    cd MiniProjetOthello
    ```

2.  **Ouvrir le projet** :
    * Lancez votre IDE (ex: IntelliJ IDEA).
    * Sélectionnez *Open* (Ouvrir) et choisissez le fichier `pom.xml` à la racine du dossier cloné.
    * Laissez votre IDE télécharger automatiquement toutes les dépendances JavaFX et SQLite nécessaires.

3.  **Lancer le jeu** :
    * **Via l'IDE :** Cherchez la classe principale contenant la méthode `main` (dans `Launcher.java` ou `Main.java`) et cliquez sur la flèche verte **Run**.
    * **Via le terminal (sans rien installer d'autre) :** Grâce au wrapper Maven inclus dans le projet, tapez simplement la commande suivante dans votre terminal :
        * Sur Windows (PowerShell/CMD) :
            ```powershell
            ./mvnw javafx:run
            ```
        * Sur Linux/Mac :
            ```bash
            ./mvnw javafx:run
            ```

---

##  Gestion de la Base de Données

Le projet utilise **SQLite**. Lors du premier lancement du jeu sur une nouvelle machine, le programme va automatiquement créer un fichier local nommé `othello_game.db` à la racine du projet. 
* Ce fichier sert à stocker de manière persistante vos statistiques de jeu.
* Il est configuré pour être ignoré par Git (`.gitignore`), garantissant que chaque joueur dispose de son propre historique de scores en local sans écraser celui des autres.
