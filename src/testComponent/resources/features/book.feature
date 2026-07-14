Feature: Gestion des livres

  Scenario: Ajouter un livre puis le retrouver dans la liste
    Given aucun livre n'est enregistré
    When j'ajoute le livre "Clean Code" de "Robert C. Martin"
    Then la liste des livres contient "Clean Code" de "Robert C. Martin"

  Scenario: La liste des livres est triée par ordre alphabétique
    Given aucun livre n'est enregistré
    When j'ajoute le livre "Refactoring" de "Martin Fowler"
    And j'ajoute le livre "Clean Code" de "Robert C. Martin"
    Then la liste des livres est triée par titre
