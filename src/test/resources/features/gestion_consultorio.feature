Feature: Gestion de consultorio medico
  As a personal de consultorio
  I want gestionar el acceso y la vinculacion de consultorio
  So that se respeten las reglas de rol y disponibilidad

  @gestion_consultorio @positivo
  Scenario: Medico se vincula a consultorio disponible
    Given existe un medico registrado con rol "medico" y credenciales validas
    And existe al menos un consultorio en estado SinMedico disponible para ser tomado
    When el medico navega a la pantalla de login
    And el medico ingresa sus credenciales validas y envia el formulario
    And el medico selecciona un consultorio disponible y confirma la vinculacion
    Then el consultorio cambia a ConMedicoDisponible y se visualiza correctamente en la UI

  @gestion_consultorio @negativo
  Scenario: Usuario sin rol medico recibe acceso denegado al panel medico
    Given existe un usuario registrado con rol "employee" y credenciales validas
    When el usuario navega a la pantalla de login
    And el usuario ingresa sus credenciales y envia el formulario
    Then el sistema muestra el mensaje de acceso denegado "Insufficient role" y no permite ingresar al panel medico