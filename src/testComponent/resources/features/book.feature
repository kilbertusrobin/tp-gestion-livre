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

  Scenario: Réserver un livre disponible
    Given aucun livre n'est enregistré
    When j'ajoute le livre "Clean Code" de "Robert C. Martin"
    And je réserve ce livre
    Then ce livre n'est plus disponible dans la liste

  Scenario: Un livre déjà réservé ne peut pas être réservé à nouveau
    Given aucun livre n'est enregistré
    When j'ajoute le livre "Clean Code" de "Robert C. Martin"
    And je réserve ce livre
    Then la réservation de ce livre échoue avec le statut 409
