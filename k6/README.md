# Tests de performance k6

Prérequis : application déployée et accessible (par défaut sur `http://localhost:8080`).

## Installation de k6

```bash
# macOS
brew install k6

# Linux (Debian/Ubuntu)
sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv-keys C5AD17C747E3415A3642D57D77C6C491D6AC1D69
echo "deb https://dl.k6.io/deb stable main" | sudo tee /etc/apt/sources.list.d/k6.list
sudo apt-get update
sudo apt-get install k6
```

## Exécution

```bash
k6 run k6/create-and-list-books.js

# avec une URL différente
BASE_URL=http://mon-serveur:8080 k6 run k6/create-and-list-books.js
```

Le script `create-and-list-books.js` fait alterner aléatoirement des créations de livres
(`POST /books`) et des récupérations de la liste (`GET /books`), avec 10 utilisateurs
virtuels pendant 30 secondes.
