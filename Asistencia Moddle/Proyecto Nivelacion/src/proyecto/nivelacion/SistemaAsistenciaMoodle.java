package proyecto.nivelacion;
import java.util.Scanner;


public class SistemaAsistenciaMoodle {

    static Scanner sc = new Scanner(System.in);

  
    static final int MAX_EST = 5;
    static final int MAX_ASIG = 4;
    static final int MAX_CLASES = 16;
    static final int UMBRAL_RACHA_DESTACADO = 5; 

    static String[] est_codigo = new String[MAX_EST + 1];
    static String[] est_nombre = new String[MAX_EST + 1];
    static String[] est_carrera = new String[MAX_EST + 1];
    static String[] est_curso = new String[MAX_EST + 1];
    static String[] est_paralelo = new String[MAX_EST + 1];
    static String[] est_matricula = new String[MAX_EST + 1];
    static String[] est_usuario = new String[MAX_EST + 1];
    static String[] est_clave = new String[MAX_EST + 1];
    static int cant_estudiantes = 0;
    
    
    static String[] asig_codigo = new String[MAX_ASIG + 1];
    static String[] asig_nombre = new String[MAX_ASIG + 1];
    static String[] asig_docente = new String[MAX_ASIG + 1];
    static int[] asig_total_clases = new int[MAX_ASIG + 1];  
    static double[] asig_porcentaje_minimo = new double[MAX_ASIG + 1];
    static String[] asig_horario = new String[MAX_ASIG + 1];
    static double[] asig_duracion_horas = new double[MAX_ASIG + 1]; 
    static int[] asig_clases_realizadas = new int[MAX_ASIG + 1];  
    static boolean[] asig_clase_abierta = new boolean[MAX_ASIG + 1];
    static int[] asig_clase_actual = new int[MAX_ASIG + 1]; 
    static int cant_asignaturas = 0;

   
    static int[][] asistencias = new int[MAX_ASIG + 1][MAX_EST + 1];
    static int[][] faltas = new int[MAX_ASIG + 1][MAX_EST + 1];
    static int[][] atrasos = new int[MAX_ASIG + 1][MAX_EST + 1];
    static int[][] justificaciones = new int[MAX_ASIG + 1][MAX_EST + 1];
    static String[][] estado_actual = new String[MAX_ASIG + 1][MAX_EST + 1]; 
    static String[][] estado_academico = new String[MAX_ASIG + 1][MAX_EST + 1];
    static int[][] racha_perfecta = new int[MAX_ASIG + 1][MAX_EST + 1];

   
    static String[][][] matriz = new String[MAX_ASIG + 1][MAX_EST + 1][MAX_CLASES + 1];
    static String[][] fecha_clase = new String[MAX_ASIG + 1][MAX_CLASES + 1];

    static final String[] CODIGOS_QR = {"", "1234", "4321", "1111", "0000"}; 

    public static void main(String[] args) {
        inicializarDatos();

        int opcion_rol;
        do {
            limpiarPantalla();
            System.out.println("========================================");
            System.out.println("  SISTEMA DE ASISTENCIA ACADEMICA MOODLE ");
            System.out.println("========================================");
            System.out.println("1. Ingresar como Docente");
            System.out.println("2. Ingresar como Estudiante");
            System.out.println("3. Salir del Sistema");
            System.out.println("========================================");
            System.out.print("Seleccione su rol: ");
            opcion_rol = leerEntero();

            if (opcion_rol == 1) {
                limpiarPantalla();
                System.out.println("--- LOGIN DOCENTE ---");
                System.out.print("Usuario: ");
                String usuario = sc.nextLine();
                System.out.print("Contrasena: ");
                String clave = sc.nextLine();

                if (usuario.equals("Mfernan23") && clave.equals("123")) {
                    menuDocente();
                } else {
                    System.out.println("Credenciales incorrectas. Acceso denegado.");
                    esperar(1);
                }
            } else if (opcion_rol == 2) {
                limpiarPantalla();
                System.out.println("--- LOGIN ESTUDIANTE ---");
                System.out.print("Usuario: ");
                String usuario = sc.nextLine();
                System.out.print("Contrasena: ");
                String clave = sc.nextLine();

                int idEst = validarCredenciales(usuario, clave);
                if (idEst != 0) {
                    menuEstudiante(idEst);
                } else {
                    System.out.println("Credenciales incorrectas o estudiante no matriculado.");
                    esperar(1);
                }
            }

        } while (opcion_rol != 3);

        System.out.println("Saliendo del sistema...");
    }

   
    static void inicializarDatos() {
        cant_estudiantes = 5;
        String[][] datosEst = {
                {"E-001", "Ana Torres", "Ing. en Software", "3ro", "A", "MAT-2026-001", "usuario1"},
                {"E-002", "Luis Mendoza", "Ing. en Software", "3ro", "A", "MAT-2026-002", "usuario2"},
                {"E-003", "Maria Lopez", "Ing. en Software", "3ro", "B", "MAT-2026-003", "usuario3"},
                {"E-004", "Carlos Ruiz", "Ing. en Software", "3ro", "B", "MAT-2026-004", "usuario4"},
                {"E-005", "Sofia Vera", "Ing. en Software", "3ro", "A", "MAT-2026-005", "usuario5"}
        };
        for (int i = 1; i <= cant_estudiantes; i++) {
            est_codigo[i] = datosEst[i - 1][0];
            est_nombre[i] = datosEst[i - 1][1];
            est_carrera[i] = datosEst[i - 1][2];
            est_curso[i] = datosEst[i - 1][3];
            est_paralelo[i] = datosEst[i - 1][4];
            est_matricula[i] = datosEst[i - 1][5];
            est_usuario[i] = datosEst[i - 1][6];
            est_clave[i] = "123";
        }

        cant_asignaturas = 4;
        Object[][] datosAsig = {
                {"COD-101", "Arquitectura de Computadoras", "Matias Fernandez", 16, 80.0, "Lunes 14:00-16:00", 2.0},
                {"COD-102", "Programacion", "Matias Fernandez", 16, 70.0, "Martes 10:00-12:00", 2.0},
                {"COD-103", "Bases de Datos", "Matias Fernandez", 16, 75.0, "Miercoles 08:00-10:00", 2.0},
                {"COD-104", "Redes", "Matias Fernandez", 16, 80.0, "Jueves 16:00-18:00", 2.0}
        };
        for (int a = 1; a <= cant_asignaturas; a++) {
            asig_codigo[a] = (String) datosAsig[a - 1][0];
            asig_nombre[a] = (String) datosAsig[a - 1][1];
            asig_docente[a] = (String) datosAsig[a - 1][2];
            asig_total_clases[a] = (Integer) datosAsig[a - 1][3];
            asig_porcentaje_minimo[a] = (Double) datosAsig[a - 1][4];
            asig_horario[a] = (String) datosAsig[a - 1][5];
            asig_duracion_horas[a] = (Double) datosAsig[a - 1][6];
            asig_clases_realizadas[a] = 0;
            asig_clase_abierta[a] = false;
            asig_clase_actual[a] = 0;
        }

        for (int a = 1; a <= MAX_ASIG; a++) {
            for (int e = 1; e <= MAX_EST; e++) {
                asistencias[a][e] = 0;
                faltas[a][e] = 0;
                atrasos[a][e] = 0;
                justificaciones[a][e] = 0;
                estado_actual[a][e] = "Sin registrar";
                estado_academico[a][e] = "Activo";
                racha_perfecta[a][e] = 0;
                for (int c = 1; c <= MAX_CLASES; c++) {
                    matriz[a][e][c] = " ";
                }
            }
        }
    }

   
    static int validarCredenciales(String usuario, String clave) {
        int indice = 0;
        for (int i = 1; i <= cant_estudiantes; i++) {
            if (est_usuario[i].equals(usuario) && est_clave[i].equals(clave)) {
                indice = i;
            }
        }
        return indice;
    }

    static int buscarEstudiantePorCodigo(String codigo) {
        for (int i = 1; i <= cant_estudiantes; i++) {
            if (est_codigo[i].equalsIgnoreCase(codigo)) return i;
        }
        return 0;
    }

    static int buscarAsignaturaPorCodigo(String codigo) {
        for (int i = 1; i <= cant_asignaturas; i++) {
            if (asig_codigo[i].equalsIgnoreCase(codigo)) return i;
        }
        return 0; 
    }

    
    static void menuDocente() {
        int opcion;
        do {
            limpiarPantalla();
            System.out.println("========================================");
            System.out.println("             PANEL DE DOCENTE            ");
            System.out.println("========================================");
            System.out.println("1. Gestionar Estudiantes");
            System.out.println("2. Gestionar Asignaturas");
            System.out.println("3. Dictar una Clase (abrir codigo QR)");
            System.out.println("4. Cerrar Clase Actual (registrar ausencias automaticas)");
            System.out.println("5. Registrar Justificacion");
            System.out.println("6. Modificar Asistencia Manualmente");
            System.out.println("7. Ver Reportes");
            System.out.println("8. Ver Prediccion Academica de un Estudiante");
            System.out.println("9. Ver Tabla de Verdad de las Reglas de Alerta");
            System.out.println("10. Conversion Horas Academicas <-> Clases");
            System.out.println("11. Cerrar Sesion");
            System.out.print("Seleccione una opcion: ");
            opcion = leerEntero();

            switch (opcion) {
                case 1: gestionarEstudiantes(); break;
                case 2: gestionarAsignaturas(); break;
                case 3: dictarClase(); break;
                case 4: cerrarClase(); break;
                case 5: registrarJustificacion(); break;
                case 6: modificarAsistencia(); break;
                case 7: menuReportes(); break;
                case 8: menuPrediccion(); break;
                case 9: mostrarTablaVerdad(); break;
                case 10: conversionUnidades(); break;
                default: break;
            }
        } while (opcion != 11);
    }

    
    static void gestionarEstudiantes() {
        int opcion;
        do {
            limpiarPantalla();
            System.out.println("--- GESTION DE ESTUDIANTES ---");
            System.out.println("1. Listar estudiantes");
            System.out.println("2. Registrar nuevo estudiante");
            System.out.println("3. Volver");
            System.out.print("Opcion: ");
            opcion = leerEntero();

            if (opcion == 1) {
                limpiarPantalla();
                System.out.println("--- LISTA DE ESTUDIANTES ---");
                for (int i = 1; i <= cant_estudiantes; i++) {
                    System.out.println(i + ". [" + est_codigo[i] + "] " + est_nombre[i] +
                            " | " + est_carrera[i] + " | " + est_curso[i] + " " + est_paralelo[i] +
                            " | Matricula: " + est_matricula[i]);
                }
                System.out.println("Presione enter para continuar...");
                esperarTecla();
            } else if (opcion == 2) {
                registrarEstudiante();
            }
        } while (opcion != 3);
    }

 
    static void registrarEstudiante() {
        limpiarPantalla();
        System.out.println("--- REGISTRAR NUEVO ESTUDIANTE ---");

        if (cant_estudiantes >= MAX_EST) {
            System.out.println("No se pueden registrar mas estudiantes (limite: " + MAX_EST + ").");
            esperar(1);
            return;
        }

        System.out.print("Codigo del estudiante (ej. E-006): ");
        String codigo = sc.nextLine();

        
        if (buscarEstudiantePorCodigo(codigo) != 0) {
            System.out.println("ERROR: Ya existe un estudiante con ese codigo. Registro cancelado.");
            esperar(2);
            return;
        }

        System.out.print("Nombre: ");
        String nombre = sc.nextLine();
        System.out.print("Carrera: ");
        String carrera = sc.nextLine();
        System.out.print("Curso: ");
        String curso = sc.nextLine();
        System.out.print("Paralelo: ");
        String paralelo = sc.nextLine();
        System.out.print("Numero de matricula: ");
        String matricula = sc.nextLine();
        System.out.print("Usuario (login): ");
        String usuario = sc.nextLine();
        System.out.print("Clave (login): ");
        String clave = sc.nextLine(); 

        cant_estudiantes = cant_estudiantes + 1;
        int i = cant_estudiantes;
        est_codigo[i] = codigo;
        est_nombre[i] = nombre;
        est_carrera[i] = carrera;
        est_curso[i] = curso;
        est_paralelo[i] = paralelo;
        est_matricula[i] = matricula;
        est_usuario[i] = usuario;
        est_clave[i] = clave;

        for (int a = 1; a <= MAX_ASIG; a++) {
            asistencias[a][i] = 0;
            faltas[a][i] = 0;
            atrasos[a][i] = 0;
            justificaciones[a][i] = 0;
            estado_actual[a][i] = "Sin registrar";
            estado_academico[a][i] = "Activo";
            racha_perfecta[a][i] = 0;
            for (int c = 1; c <= MAX_CLASES; c++) matriz[a][i][c] = " ";
        }

        System.out.println("Estudiante registrado correctamente.");
        esperar(2);
    }

    
    static void gestionarAsignaturas() {
        int opcion;
        do {
            limpiarPantalla();
            System.out.println("--- GESTION DE ASIGNATURAS ---");
            System.out.println("1. Listar asignaturas");
            System.out.println("2. Registrar nueva asignatura");
            System.out.println("3. Volver");
            System.out.print("Opcion: ");
            opcion = leerEntero();

            if (opcion == 1) {
                limpiarPantalla();
                System.out.println("--- LISTA DE ASIGNATURAS ---");
                for (int a = 1; a <= cant_asignaturas; a++) {
                    System.out.println(a + ". [" + asig_codigo[a] + "] " + asig_nombre[a] +
                            " | Docente: " + asig_docente[a] + " | Horario: " + asig_horario[a]);
                    System.out.println("   Clases dictadas: " + asig_clases_realizadas[a] + "/" + asig_total_clases[a] +
                            " | % minimo requerido: " + asig_porcentaje_minimo[a] + "%");
                    double horasTotales = convertirClasesAHoras(asig_total_clases[a], asig_duracion_horas[a]);
                    System.out.println("   Equivalencia: " + asig_total_clases[a] + " clases x " + asig_duracion_horas[a] +
                            "h = " + horasTotales + " horas academicas en el periodo.");
                }
                System.out.println("Presione enter para continuar...");
                esperarTecla();
            } else if (opcion == 2) {
                registrarAsignatura();
            }
        } while (opcion != 3);
    }

   
    static void registrarAsignatura() {
        limpiarPantalla();
        System.out.println("--- REGISTRAR NUEVA ASIGNATURA ---");

        if (cant_asignaturas >= MAX_ASIG) {
            System.out.println("No se pueden registrar mas asignaturas (limite: " + MAX_ASIG + ").");
            esperar(1);
            return;
        }

        System.out.print("Codigo de asignatura (ej. COD-105): ");
        String codigo = sc.nextLine();

        if (buscarAsignaturaPorCodigo(codigo) != 0) {
            System.out.println("ERROR: Ya existe una asignatura con ese codigo. Registro cancelado.");
            esperar(2);
            return;
        }

        System.out.print("Nombre: ");
        String nombre = sc.nextLine();
        System.out.print("Docente: ");
        String docente = sc.nextLine();
        System.out.print("Numero total de clases del periodo: ");
        int total = leerEntero();
        System.out.print("Porcentaje minimo de asistencia requerido: ");
        double minimo = leerReal();
        System.out.print("Horario: ");
        String horario = sc.nextLine();
        System.out.print("Duracion de cada clase (horas academicas): ");
        double duracion = leerReal();

        cant_asignaturas = cant_asignaturas + 1;
        int a = cant_asignaturas;
        asig_codigo[a] = codigo;
        asig_nombre[a] = nombre;
        asig_docente[a] = docente;
        asig_total_clases[a] = Math.min(total, MAX_CLASES);
        asig_porcentaje_minimo[a] = minimo;
        asig_horario[a] = horario;
        asig_duracion_horas[a] = duracion;
        asig_clases_realizadas[a] = 0;
        asig_clase_abierta[a] = false;
        asig_clase_actual[a] = 0;

        for (int e = 1; e <= MAX_EST; e++) {
            asistencias[a][e] = 0;
            faltas[a][e] = 0;
            atrasos[a][e] = 0;
            justificaciones[a][e] = 0;
            estado_actual[a][e] = "Sin registrar";
            estado_academico[a][e] = "Activo";
            racha_perfecta[a][e] = 0;
            for (int c = 1; c <= MAX_CLASES; c++) matriz[a][e][c] = " ";
        }

        System.out.println("Asignatura registrada correctamente.");
        esperar(2);
    }

  
    static int elegirAsignatura() {
        System.out.println("----------------------------------------");
        for (int a = 1; a <= cant_asignaturas; a++) {
            System.out.println(a + ". " + asig_nombre[a] + " [" + asig_codigo[a] + "]");
        }
        System.out.println("----------------------------------------");
        System.out.print("Seleccione una asignatura: ");
        int a = leerEntero();
        if (a < 1 || a > cant_asignaturas) {
            System.out.println("Asignatura no valida.");
            return 0;
        }
        return a;
    }

    
    static void dictarClase() {
        limpiarPantalla();
        System.out.println("--- DICTAR UNA CLASE ---");
        int a = elegirAsignatura();
        if (a == 0) { esperar(2); return; }

        if (asig_clase_abierta[a]) {
            System.out.println("ERROR: Ya existe una clase abierta para " + asig_nombre[a] +
                    ". Debe cerrarla antes de abrir una nueva.");
            esperar(2);
            return;
        }
        if (asig_clases_realizadas[a] >= asig_total_clases[a]) {
            System.out.println("ERROR: Ya se dictaron todas las clases programadas para esta asignatura.");
            esperar(2);
            return;
        }

        System.out.print("Ingrese la fecha de la clase (DD/MM/AAAA): ");
        String fecha = sc.nextLine();
        if (!validarFecha(fecha)) {
            System.out.println("ERROR: Formato de fecha no valido. Use DD/MM/AAAA.");
            esperar(2);
            return;
        }

        asig_clases_realizadas[a] = asig_clases_realizadas[a] + 1;
        int numClase = asig_clases_realizadas[a];
        asig_clase_actual[a] = numClase;
        asig_clase_abierta[a] = true;
        fecha_clase[a][numClase] = fecha;

        for (int e = 1; e <= cant_estudiantes; e++) {
            estado_actual[a][e] = "Pendiente";
            matriz[a][e][numClase] = " ";
        }

        limpiarPantalla();
        System.out.println("--- GENERANDO CODIGO QR PARA LA CLASE ---");
        System.out.println("Asignatura: " + asig_nombre[a] + " | Clase N#: " + numClase + " | Fecha: " + fecha);
        System.out.println("Horario: " + asig_horario[a]);
        System.out.println("El codigo cambiara cada 10 segundos.");
        for (int i = 1; i <= 4; i++) {
            System.out.println("Codigo actual para la pizarra: " + CODIGOS_QR[i]);
            esperar(2);
        }
        System.out.println("Clase abierta. Los estudiantes ya pueden registrar su asistencia.");
        System.out.println("Recuerde cerrar la clase (opcion 4) cuando termine para registrar las ausencias automaticas.");
        esperar(5);
    }

    static void cerrarClase() {
        limpiarPantalla();
        System.out.println("--- CERRAR CLASE ACTUAL ---");
        int a = elegirAsignatura();
        if (a == 0) { esperar(2); return; }

        if (!asig_clase_abierta[a]) {
            System.out.println("ERROR: No hay ninguna clase abierta en " + asig_nombre[a] + ".");
            esperar(2);
            return;
        } 

        int numClase = asig_clase_actual[a];
        for (int e = 1; e <= cant_estudiantes; e++) {
            if (estado_actual[a][e].equals("Pendiente")) {
                estado_actual[a][e] = "Ausente";
                faltas[a][e] = faltas[a][e] + 1;
                matriz[a][e][numClase] = "F";
                racha_perfecta[a][e] = 0;
            }
            actualizarEstadoAcademico(a, e);
        }

        asig_clase_abierta[a] = false;
        System.out.println("Clase cerrada. Se registraron las ausencias automaticas de quienes no marcaron asistencia.");
        esperar(2);
    }

   
    static void menuEstudiante(int idEst) {
        int opcion;
        do {
            limpiarPantalla();
            System.out.println("========================================");
            System.out.println("          PANEL DE ESTUDIANTE            ");
            System.out.println("  Bienvenido: " + est_nombre[idEst] + " (" + est_codigo[idEst] + ")");
            System.out.println("========================================");
            System.out.println("1. Ver mis Asignaturas y Horarios");
            System.out.println("2. Registrar Asistencia con Codigo QR");
            System.out.println("3. Ver mi Historial y Alerta Academica");
            System.out.println("4. Ver mi Prediccion Academica");
            System.out.println("5. Cerrar Sesion");
            System.out.print("Seleccione una opcion: ");
            opcion = leerEntero();

            switch (opcion) {
                case 1:
                    limpiarPantalla();
                    System.out.println("--- MIS ASIGNATURAS ---");
                    for (int a = 1; a <= cant_asignaturas; a++) {
                        System.out.println(a + ". " + asig_nombre[a] + " | Docente: " + asig_docente[a] +
                                " | Horario: " + asig_horario[a]);
                    }
                    System.out.println("Presione enter para volver...");
                    esperarTecla();
                    break;

                case 2:
                    limpiarPantalla();
                    System.out.println("--- REGISTRAR ASISTENCIA ---");
                    int asigReg = elegirAsignatura();
                    if (asigReg != 0) {
                        registrarAsistenciaEstudiante(asigReg, idEst);
                    } else {
                        esperar(2);
                    }
                    break;

                case 3:
                    limpiarPantalla();
                    System.out.println("--- MI HISTORIAL ---");
                    int asigHist = elegirAsignatura();
                    if (asigHist != 0) {
                        reporte1HistorialEstudiante(asigHist, idEst);
                    } else {
                        esperar(2);
                    }
                    break;

                case 4:
                    limpiarPantalla();
                    System.out.println("--- MI PREDICCION ACADEMICA ---");
                    int asigPred = elegirAsignatura();
                    if (asigPred != 0) {
                        mostrarPrediccion(asigPred, idEst);
                    } else {
                        esperar(2);
                    }
                    break;

                default:
                    break;
            }

        } while (opcion != 5);
    }

   
    static void registrarAsistenciaEstudiante(int a, int e) {
        limpiarPantalla();
        System.out.println("--- REGISTRO DE ASISTENCIA: " + asig_nombre[a] + " ---");

        if (!asig_clase_abierta[a]) {
            System.out.println("ERROR: No hay ninguna clase activa en este momento para esta asignatura.");
            esperar(3);
            return;
        }

        if (!estado_actual[a][e].equals("Pendiente")) {
            System.out.println("ERROR: Usted ya registro su asistencia para esta clase.");
            System.out.println("Estado actual registrado: " + estado_actual[a][e]);
            esperar(3);
            return; 
        }

        int numClase = asig_clase_actual[a];

        System.out.println("La clase inicia a las 00 minutos.");
        System.out.println("Ingrese el minuto en el que esta registrando la asistencia:");
        System.out.println("Ejemplo: 5 para el minuto 5, 12 para el minuto 12:");
        int minutoLlegada = leerEntero();

        System.out.println("Ingrese el codigo QR que aparece en la pizarra del docente:");
        String codigoIngresado = sc.nextLine();

        boolean qrValido = false;
        for (int i = 1; i <= 4; i++) {
            if (codigoIngresado.equals(CODIGOS_QR[i])) qrValido = true;
        }

        if (qrValido) {
            System.out.println("Ingrese cuantos segundos han pasado desde que aparecio el QR:");
            System.out.println("Recuerde que el QR solo dura 10 segundos.");
            int segundosQr = leerEntero();

            if (segundosQr <= 10) {
                if (minutoLlegada <= 10) {
                    estado_actual[a][e] = "Presente";
                    matriz[a][e][numClase] = "A";
                    asistencias[a][e] = asistencias[a][e] + 1;
                    racha_perfecta[a][e] = racha_perfecta[a][e] + 1;
                    System.out.println("QR valido y dentro del tiempo permitido.");
                    System.out.println("Asistencia registrada como PRESENTE.");
                } else {
                    estado_actual[a][e] = "Atrasado";
                    matriz[a][e][numClase] = "T";
                    atrasos[a][e] = atrasos[a][e] + 1;
                    asistencias[a][e] = asistencias[a][e] + 1;
                    racha_perfecta[a][e] = 0; 
                    System.out.println("QR valido, pero el estudiante llego tarde.");
                    System.out.println("Asistencia registrada como ATRASADO.");
                }
            } else {
                estado_actual[a][e] = "Ausente";
                matriz[a][e][numClase] = "F";
                faltas[a][e] = faltas[a][e] + 1;
                racha_perfecta[a][e] = 0;
                System.out.println("El codigo QR se paso del tiempo permitido.");
                System.out.println("Asistencia registrada como AUSENTE.");
            }
        } else {
            estado_actual[a][e] = "Ausente";
            matriz[a][e][numClase] = "F";
            faltas[a][e] = faltas[a][e] + 1;
            racha_perfecta[a][e] = 0;
            System.out.println("Codigo QR incorrecto.");
            System.out.println("Asistencia registrada como AUSENTE.");
        }

        actualizarEstadoAcademico(a, e);
        esperar(3);
    }

   
    static void registrarJustificacion() {
        limpiarPantalla();
        System.out.println("--- REGISTRAR JUSTIFICACION ---");
        int a = elegirAsignatura();
        if (a == 0) { esperar(2); return; }

        System.out.print("Ingrese el numero de lista del estudiante (1 a " + cant_estudiantes + "): ");
        int e = leerEntero();
        if (e < 1 || e > cant_estudiantes) {
            System.out.println("Estudiante no encontrado.");
            esperar(2);
            return;
        }

        System.out.print("Ingrese el numero de clase a justificar (1 a " + asig_clases_realizadas[a] + "): ");
        int numClase = leerEntero();

       
        if (numClase < 1 || numClase > asig_clases_realizadas[a] || !matriz[a][e][numClase].equals("F")) {
            System.out.println("ERROR: Esa clase no tiene una falta registrada para justificar.");
            esperar(2);
            return;
        }

        System.out.print("La falta corresponde a una actividad autorizada? (S/N): ");
        String resp = sc.nextLine();

                
        if (resp.equalsIgnoreCase("S")) {
            matriz[a][e][numClase] = "J";
            faltas[a][e] = faltas[a][e] - 1;
            justificaciones[a][e] = justificaciones[a][e] + 1;
            actualizarEstadoAcademico(a, e);
            System.out.println("Justificacion registrada. La falta ya no se contabiliza como inasistencia.");
        } else {
            System.out.println("No se aplico la Regla 2 (requiere justificacion Y actividad autorizada). La falta se mantiene.");
        }
        esperar(2);
    }

    
    static void modificarAsistencia() {
        limpiarPantalla();
        System.out.println("--- MODIFICAR ASISTENCIA MANUALMENTE ---");
        int a = elegirAsignatura();
        if (a == 0) { esperar(2); return; }

        System.out.print("Ingrese el numero de lista del estudiante (1 a " + cant_estudiantes + "): ");
        int e = leerEntero();

        if (e < 1 || e > cant_estudiantes) {
            System.out.println("Estudiante no encontrado.");
            esperar(2);
            return;
        }

        System.out.println("Estudiante seleccionado: " + est_nombre[e]);
        System.out.println("Estado actual (clase en curso): " + estado_actual[a][e]);
        System.out.println("Seleccione el nuevo estado:");
        System.out.println("1. Presente");
        System.out.println("2. Atrasado");
        System.out.println("3. Ausente");
        System.out.println("4. Justificado");
        int nuevoEstado = leerEntero();

        int numClase = asig_clase_actual[a];
        if (numClase < 1) {
            System.out.println("ERROR: No existe una clase registrada aun para esta asignatura.");
            esperar(2);
            return;
        }

        switch (nuevoEstado) {
            case 1:
                estado_actual[a][e] = "Presente";
                matriz[a][e][numClase] = "A";
                asistencias[a][e] = asistencias[a][e] + 1;
                if (faltas[a][e] > 0) faltas[a][e] = faltas[a][e] - 1;
                break;
            case 2:
                estado_actual[a][e] = "Atrasado";
                matriz[a][e][numClase] = "T";
                atrasos[a][e] = atrasos[a][e] + 1;
                if (faltas[a][e] > 0) faltas[a][e] = faltas[a][e] - 1;
                asistencias[a][e] = asistencias[a][e] + 1;
                break;
            case 3:
                estado_actual[a][e] = "Ausente";
                matriz[a][e][numClase] = "F";
                faltas[a][e] = faltas[a][e] + 1;
                break;
            case 4:
                estado_actual[a][e] = "Justificado";
                matriz[a][e][numClase] = "J";
                justificaciones[a][e] = justificaciones[a][e] + 1;
                if (atrasos[a][e] > 0) atrasos[a][e] = atrasos[a][e] - 1;
                if (faltas[a][e] > 0) faltas[a][e] = faltas[a][e] - 1;
                break;
            default:
                System.out.println("Opcion no valida.");
                esperar(2);
                return;
        }

        actualizarEstadoAcademico(a, e);
        System.out.println("Registro actualizado correctamente.");
        esperar(2);
    }

   
    static double calcularPorcentajeAsistencia(int a, int e) {
        if (asig_clases_realizadas[a] == 0) return 0.0;
        double pct = ((double) asistencias[a][e] / asig_clases_realizadas[a]) * 100;
        return limitarPorcentaje(pct);
    }

    //basic %//
    static double calcularPorcentaje(int cantidad, int total) {
        if (total == 0) return 0.0;
        return limitarPorcentaje(((double) cantidad / total) * 100);
    }

     //0 a 100//
    static double limitarPorcentaje(double x) {
        if (x > 100.0) return 100.0;
        if (x < 0.0) return 0.0;
        return x;
    }

    
    static void mostrarFormulaPorcentaje(String etiqueta, int cantidad, int total, double resultado) {
        System.out.println("   " + etiqueta + " = (" + cantidad + " / " + total + ") x 100 = " +
                redondear(resultado) + "%");
    }

    static double redondear(double x) {
        return Math.round(x * 100.0) / 100.0; // 2 deci//
    }

   //deci a ente//
    static int calcularFaltasPermitidas(int a) {
        double permitidas = asig_total_clases[a] * (100 - asig_porcentaje_minimo[a]) / 100.0;
        return (int) Math.floor(permitidas);
    }

   
    static boolean puedeAprobarMatematicamente(int a, int e) {
        int clasesRestantes = asig_total_clases[a] - asig_clases_realizadas[a];
        int maxAsistenciasPosibles = asistencias[a][e] + clasesRestantes;
        double mejorPctPosible = calcularPorcentaje(maxAsistenciasPosibles, asig_total_clases[a]);
        return mejorPctPosible >= asig_porcentaje_minimo[a];
    }
   
  //agarrar Mayor//
    static int calcularClasesPuedeFaltar(int a, int e) {
        int permitidas = calcularFaltasPermitidas(a);
        int disponibles = permitidas - faltas[a][e];
        return Math.max(disponibles, 0);
    }

    
    static int calcularAsistenciaConsecutivaNecesaria(int a, int e) {
        double minimo = asig_porcentaje_minimo[a] / 100.0;
        double realizadas = asig_clases_realizadas[a];
        double asist = asistencias[a][e];

        double pctActual = calcularPorcentajeAsistencia(a, e);
        if (pctActual >= asig_porcentaje_minimo[a]) return 0; 

        if (minimo >= 1.0) {
           
            int restantes = asig_total_clases[a] - asig_clases_realizadas[a];
            return restantes; 
        }

        double numerador = (minimo * realizadas) - asist;
        double denominador = 1 - minimo;
        double x = numerador / denominador;

       //redondea arriba// 
        int xEntero = (int) Math.ceil(x - 1e-9); 
        if (xEntero < 0) xEntero = 0; //si tienes sufucientes asis//

        int clasesRestantes = asig_total_clases[a] - asig_clases_realizadas[a];
        if (xEntero > clasesRestantes) return -1; // matematicamente ya no es posible alcanzar el minimo con las clases restantes

        return xEntero; 
    }

   //2 deci//
    static int convertirHorasAClases(double horas, double duracionClase) {
        if (duracionClase <= 0) return 0;
        return (int) Math.round(horas / duracionClase);
    }

  
    static double convertirClasesAHoras(int clases, double duracionClase) {
        return clases * duracionClase;
    }

   
    //hermoso w//
    static boolean validarFecha(String fecha) {
        if (fecha.length() != 10) return false;
        if (fecha.charAt(2) != '/' && fecha.charAt(5) != '/') return false;
        try {
            int dia = Integer.parseInt(fecha.substring(0, 2)); //1. text a num//
            int mes = Integer.parseInt(fecha.substring(3, 5));  //2. posicion//
            int anio = Integer.parseInt(fecha.substring(6, 10));
            if (dia < 1 || dia > 31) return false;
            if (mes < 1 || mes > 12) return false;
            if (anio < 2000 || anio > 2100) return false;
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

   
  
    static String clasificarEstudiante(int a, int e) {
        double pct = calcularPorcentajeAsistencia(a, e);
        double minimo = asig_porcentaje_minimo[a];
        int faltasPermitidas = calcularFaltasPermitidas(a);
        boolean puedeAprobar = puedeAprobarMatematicamente(a, e);

       
        if (faltas[a][e] > faltasPermitidas || !puedeAprobar) {
            return "Reprobado por inasistencia";
        }

       
        if (racha_perfecta[a][e] >= UMBRAL_RACHA_DESTACADO) {
            return "Destacado";
        }

       
        if (pct < minimo && justificaciones[a][e] < faltas[a][e]) {
            return "Riesgo de perdida";
        }

        return "Activo";
    }

  
    static void actualizarEstadoAcademico(int a, int e) {
        estado_academico[a][e] = clasificarEstudiante(a, e);
    }

    
    static void generarAlerta(int a, int e) {
        double pct = calcularPorcentajeAsistencia(a, e);
        String estado = estado_academico[a][e];
        int faltasPermitidas = calcularFaltasPermitidas(a);

        System.out.println("Estado academico: " + estado);

        switch (estado) {
            case "Activo":
                System.out.println("Explicacion (Regla 1, no se cumple): el estudiante registra " + redondear(pct) +
                        "% de asistencia, que es mayor o igual al " + asig_porcentaje_minimo[a] + "% minimo requerido.");
                break;
            case "Riesgo de perdida":
                System.out.println("Explicacion (Regla 1 aplicada): el estudiante fue clasificado en Riesgo Academico " +
                        "porque registra " + redondear(pct) + "% de asistencia (por debajo del " +
                        asig_porcentaje_minimo[a] + "% minimo) Y sus justificaciones (" + justificaciones[a][e] +
                        ") no cubren la cantidad de faltas (" + faltas[a][e] + "). " +
                        "Necesita mejorar su asistencia en las clases restantes para recuperar el minimo requerido.");
                break;
            case "Reprobado por inasistencia":
                System.out.println("Explicacion (Regla 3 aplicada): el estudiante tiene " + faltas[a][e] +
                        " falta(s) sobre un maximo permitido de " + faltasPermitidas +
                        ", o matematicamente ya no puede alcanzar el " + asig_porcentaje_minimo[a] +
                        "% minimo con las clases que quedan en el periodo.");
                break;
            case "Destacado":
                System.out.println("Explicacion (Regla 4 aplicada): el estudiante mantiene " + racha_perfecta[a][e] +
                        " clases consecutivas con asistencia perfecta (>= " + UMBRAL_RACHA_DESTACADO +
                        "), por lo que se reconoce como Estudiante Destacado.");
                break;
        }
    }

    
    static void menuPrediccion() {
        limpiarPantalla();
        System.out.println("--- PREDICCION ACADEMICA ---");
        int a = elegirAsignatura();
        if (a == 0) { esperar(2); return; }
        System.out.print("Ingrese el nume if (a == 0) { esperar(2); return; }ro de lista del estudiante (1 a " + cant_estudiantes + "): ");
        int e = leerEntero();
        if (e < 1 || e > cant_estudiantes) {
            System.out.println("Estudiante no encontrado.");
            esperar(2);
            return;
        }
        mostrarPrediccion(a, e);
    }

    static void mostrarPrediccion(int a, int e) {
        limpiarPantalla();
        System.out.println("--- PREDICCION ACADEMICA: " + est_nombre[e] + " / " + asig_nombre[a] + " ---");

        double pct = calcularPorcentajeAsistencia(a, e);
        System.out.println("Porcentaje de asistencia actual: " + redondear(pct) + "%");
        System.out.println("Porcentaje minimo requerido: " + asig_porcentaje_minimo[a] + "%");
        System.out.println("Clases dictadas hasta ahora: " + asig_clases_realizadas[a] + "/" + asig_total_clases[a]);
        System.out.println("----------------------------------------");

        int puedeFaltar = calcularClasesPuedeFaltar(a, e);
        System.out.println("1) Clases que aun puede faltar sin reprobar:");
        System.out.println("   Faltas permitidas totales - faltas actuales = " + calcularFaltasPermitidas(a) +
                " - " + faltas[a][e] + " = " + puedeFaltar + " clase(s).");

        int consecutivas = calcularAsistenciaConsecutivaNecesaria(a, e);
        System.out.println("2) Clases consecutivas que debe asistir para recuperar el minimo:");
        if (consecutivas == 0) {
            System.out.println("   Ya cumple el porcentaje minimo requerido, no necesita recuperar.");
        } else if (consecutivas == -1) {
            System.out.println("   Matematicamente ya no es posible alcanzar el minimo con las clases restantes.");
        } else {
            System.out.println("   Resolviendo la ecuacion lineal (asistencias + x)/(realizadas + x) >= minimo/100:");
            System.out.println("   x >= [(minimo/100 x realizadas) - asistencias] / (1 - minimo/100)");
            System.out.println("   Resultado: debe asistir a " + consecutivas + " clase(s) consecutivas.");
        }

        boolean puedeAprobar = puedeAprobarMatematicamente(a, e);
        int clasesRestantes = asig_total_clases[a] - asig_clases_realizadas[a];
        int maxAsistenciasPosibles = asistencias[a][e] + clasesRestantes;
        System.out.println("3) Puede aprobar matematicamente por asistencia?");
        System.out.println("   Mejor escenario posible: (" + asistencias[a][e] + " + " + clasesRestantes + ") / " +
                asig_total_clases[a] + " x 100 = " + redondear(calcularPorcentaje(maxAsistenciasPosibles, asig_total_clases[a])) + "%");
        System.out.println("   Respuesta: " + (puedeAprobar ? "SI, aun es posible." : "NO, ya no es matematicamente posible."));

        System.out.println("Presione enter para continuar..."); //ope ternario//
        esperarTecla();
    }

   
    static void mostrarTablaVerdad() {
        limpiarPantalla();
        System.out.println("--- TABLA DE VERDAD DE LAS REGLAS DE ALERTA ---");
        System.out.println("P = El porcentaje de asistencia es menor al minimo requerido");
        System.out.println("Q = Las justificaciones NO cubren las faltas registradas");
        System.out.println("R = El estudiante supera el numero maximo de faltas permitidas");
        System.out.println();
        System.out.println("Regla 1: P AND Q  -> genera alerta de Riesgo Academico");
        System.out.println("Regla 3: R OR (NO puede aprobar matematicamente) -> Reprobado por inasistencia");
        System.out.println();
        System.out.println(" P | Q | P AND Q (Regla 1) ");
        System.out.println("---+---+--------------------");
        for (int p = 0; p <= 1; p++) {
            for (int q = 0; q <= 1; q++) {
                boolean P = (p == 1); //f o v//
                boolean Q = (q == 1);
                boolean resultado = P && Q;
                System.out.println(" " + (P ? "V" : "F") + " | " + (Q ? "V" : "F") + " |        " + (resultado ? "V" : "F"));
            }
        }
        System.out.println();
        System.out.println(" R | P AND Q | R OR (P AND Q) ");
        System.out.println("---+---------+------------------");
        for (int r = 0; r <= 1; r++) {
            for (int pq = 0; pq <= 1; pq++) {
                boolean R = (r == 1);
                boolean PQ = (pq == 1);
                boolean resultado = R || PQ;
                System.out.println(" " + (R ? "V" : "F") + " |    " + (PQ ? "V" : "F") + "    |       " + (resultado ? "V" : "F"));
            }
        }
        System.out.println();
        System.out.println("Presione enter para continuar...");
        esperarTecla();
    }

    static void conversionUnidades() {
        limpiarPantalla();
        System.out.println("--- CONVERSION: HORAS ACADEMICAS <-> CLASES ---");
        System.out.println("1. Convertir horas a numero de clases");
        System.out.println("2. Convertir numero de clases a horas");
        System.out.print("Opcion: ");
        int op = leerEntero();

        System.out.print("Duracion de cada clase en horas (ej. 2): ");
        double duracion = leerReal();

        if (op == 1) {
            System.out.print("Cantidad de horas academicas: ");
            double horas = leerReal();
            int clases = convertirHorasAClases(horas, duracion);
            System.out.println(horas + " horas / " + duracion + " horas por clase = " + clases + " clase(s).");
        } else if (op == 2) {
            System.out.print("Cantidad de clases: ");
            int clases = leerEntero();
            double horas = convertirClasesAHoras(clases, duracion);
            System.out.println(clases + " clases x " + duracion + " horas por clase = " + horas + " horas academicas.");
        }
        System.out.println("Presione enter para continuar...");
        esperarTecla();
    }

    
    static void menuReportes() {
        int opcion;
        do {
            limpiarPantalla();
            System.out.println("--- MENU DE REPORTES ---");
            System.out.println("1. Historial de asistencia por estudiante");
            System.out.println("2. Asistencia por asignatura");
            System.out.println("3. Estudiantes con mayor asistencia");
            System.out.println("4. Estudiantes en riesgo");
            System.out.println("5. Estudiantes reprobados por inasistencia");
            System.out.println("6. Promedio de asistencia por curso");
            System.out.println("7. Promedio de asistencia por asignatura");
            System.out.println("8. Ranking de mejor asistencia");
            System.out.println("9. Cantidad de faltas justificadas");
            System.out.println("10. Estadisticas generales del periodo academico");
            System.out.println("11. Promedio de asistencia por paralelo (extra)");
            System.out.println("12. Volver");
            System.out.print("Opcion: ");
            opcion = leerEntero();

            switch (opcion) {
                case 1: {
                    int a = elegirAsignatura();
                    if (a != 0) {
                        System.out.print("Numero de estudiante (1 a " + cant_estudiantes + "): ");
                        int e = leerEntero();
                        if (e >= 1 && e <= cant_estudiantes) reporte1HistorialEstudiante(a, e);
                    }
                    break;
                }
                case 2: reporte2AsistenciaPorAsignatura(); break;
                case 3: reporte3MayorAsistencia(); break;
                case 4: reporte4EstudiantesEnRiesgo(); break;
                case 5: reporte5ReprobadosPorInasistencia(); break;
                case 6: reporte6PromedioPorCurso(); break;
                case 7: reporte7PromedioPorAsignatura(); break;
                case 8: reporte8RankingMejorAsistencia(); break;
                case 9: reporte9CantidadFaltasJustificadas(); break;
                case 10: reporte10EstadisticasGenerales(); break;
                case 11: reporte11PromedioPorParalelo(); break;
                default: break;
            }
        } while (opcion != 12);
    }

    static void reporte1HistorialEstudiante(int a, int e) {
        limpiarPantalla();
        System.out.println("--- HISTORIAL: " + est_nombre[e] + " | " + asig_nombre[a] + " ---");
        System.out.println("Codigo: " + est_codigo[e] + " | Carrera: " + est_carrera[e] +
                " | Curso: " + est_curso[e] + est_paralelo[e]);
        System.out.println("----------------------------------------");
        System.out.print("Registro por clase: ");
        for (int c = 1; c <= asig_clases_realizadas[a]; c++) {
            System.out.print("[C" + c + ":" + matriz[a][e][c] + "] ");
        }
        System.out.println();
        System.out.println("----------------------------------------");
        System.out.println("Asistencias: " + asistencias[a][e] + " | Faltas: " + faltas[a][e] +
                " | Atrasos: " + atrasos[a][e] + " | Justificaciones: " + justificaciones[a][e]);

        double pct = calcularPorcentajeAsistencia(a, e);
        mostrarFormulaPorcentaje("Porcentaje de asistencia", asistencias[a][e], asig_clases_realizadas[a], pct);
        mostrarFormulaPorcentaje("Porcentaje de faltas", faltas[a][e], asig_clases_realizadas[a],
                calcularPorcentaje(faltas[a][e], asig_clases_realizadas[a]));
        mostrarFormulaPorcentaje("Porcentaje de atrasos", atrasos[a][e], asig_clases_realizadas[a],
                calcularPorcentaje(atrasos[a][e], asig_clases_realizadas[a]));
        mostrarFormulaPorcentaje("Porcentaje justificado", justificaciones[a][e], asig_clases_realizadas[a],
                calcularPorcentaje(justificaciones[a][e], asig_clases_realizadas[a]));

        System.out.println("----------------------------------------");
        System.out.println("ALERTA ACADEMICA");
        generarAlerta(a, e);

        System.out.println("Presione enter para continuar...");
        esperarTecla();
    }

    static void reporte2AsistenciaPorAsignatura() {
        limpiarPantalla();
        System.out.println("--- ASISTENCIA POR ASIGNATURA ---");
        for (int a = 1; a <= cant_asignaturas; a++) {
            System.out.println(asig_nombre[a] + " (" + asig_clases_realizadas[a] + " clases dictadas):");
            for (int e = 1; e <= cant_estudiantes; e++) {
                double pct = calcularPorcentajeAsistencia(a, e);
                System.out.println("   " + est_nombre[e] + ": " + redondear(pct) + "% - " + estado_academico[a][e]);
            }
        }
        System.out.println("Presione enter para continuar...");
        esperarTecla();
    }

    static void reporte3MayorAsistencia() {
        limpiarPantalla();
        System.out.println("--- ESTUDIANTES CON MAYOR ASISTENCIA (promedio entre asignaturas) ---");
        for (int e = 1; e <= cant_estudiantes; e++) {
            double suma = 0; //sum %//
            int cuentaAsig = 0; // asis contadas//
            for (int a = 1; a <= cant_asignaturas; a++) {
                if (asig_clases_realizadas[a] > 0) {
                    suma += calcularPorcentajeAsistencia(a, e);
                    cuentaAsig++;
                }
            }
            double promedio = cuentaAsig > 0 ? suma / cuentaAsig : 0;
            System.out.println(est_nombre[e] + ": " + redondear(promedio) + "% de asistencia promedio.");
        }
        System.out.println("Presione enter para continuar...");
        esperarTecla();
    }

    static void reporte4EstudiantesEnRiesgo() {
        limpiarPantalla();
        System.out.println("--- ESTUDIANTES EN RIESGO ---");
        boolean hay = false;
        for (int a = 1; a <= cant_asignaturas; a++) {
            for (int e = 1; e <= cant_estudiantes; e++) {
                if (estado_academico[a][e].equals("Riesgo de perdida")) {
                    System.out.println(est_nombre[e] + " en " + asig_nombre[a] + ": " +
                            redondear(calcularPorcentajeAsistencia(a, e)) + "% de asistencia.");
                    hay = true;
                }
            }
        }
        if (!hay) System.out.println("No hay estudiantes en riesgo actualmente.");
        System.out.println("Presione enter para continuar...");
        esperarTecla();
    }

    static void reporte5ReprobadosPorInasistencia() {
        limpiarPantalla();
        System.out.println("--- ESTUDIANTES REPROBADOS POR INASISTENCIA ---");
        boolean hay = false;
        for (int a = 1; a <= cant_asignaturas; a++) {
            for (int e = 1; e <= cant_estudiantes; e++) {
                if (estado_academico[a][e].equals("Reprobado por inasistencia")) {
                    System.out.println(est_nombre[e] + " en " + asig_nombre[a] + ": " +
                            redondear(calcularPorcentajeAsistencia(a, e)) + "% de asistencia, " +
                            faltas[a][e] + " falta(s).");
                    hay = true;
                }
            }
        }
        if (!hay) System.out.println("No hay estudiantes reprobados por inasistencia actualmente.");
        System.out.println("Presione enter para continuar...");
        esperarTecla();
    }

    static void reporte6PromedioPorCurso() {
        limpiarPantalla();
        System.out.println("--- PROMEDIO DE ASISTENCIA POR CURSO ---");
        String[] cursosVistos = new String[MAX_EST + 1];
        int cantCursos = 0;

        for (int e = 1; e <= cant_estudiantes; e++) {
            String curso = est_curso[e];
            boolean yaVisto = false;
            for (int i = 1; i <= cantCursos; i++) if (cursosVistos[i].equals(curso)) yaVisto = true;
            if (!yaVisto) {
                cantCursos++;
                cursosVistos[cantCursos] = curso;
            }
        }

        for (int i = 1; i <= cantCursos; i++) {
            double suma = 0;
            int cuenta = 0;
            for (int e = 1; e <= cant_estudiantes; e++) {
                if (est_curso[e].equals(cursosVistos[i])) {
                    for (int a = 1; a <= cant_asignaturas; a++) {
                        if (asig_clases_realizadas[a] > 0) {
                            suma += calcularPorcentajeAsistencia(a, e);
                            cuenta++;
                        }
                    }
                }
            }
            double promedio = cuenta > 0 ? suma / cuenta : 0; ///no cero//
            System.out.println("Curso " + cursosVistos[i] + ": " + redondear(promedio) + "% de asistencia promedio.");
        }
        System.out.println("Presione enter para continuar...");
        esperarTecla();
    }

    static void reporte7PromedioPorAsignatura() {
        limpiarPantalla();
        System.out.println("--- PROMEDIO DE ASISTENCIA POR ASIGNATURA ---");
        for (int a = 1; a <= cant_asignaturas; a++) {
            double suma = 0;
            for (int e = 1; e <= cant_estudiantes; e++) {
                suma += calcularPorcentajeAsistencia(a, e);
            }
            double promedio = cant_estudiantes > 0 ? suma / cant_estudiantes : 0;
            System.out.println(asig_nombre[a] + ": " + redondear(promedio) + "% de asistencia promedio.");
        }
        System.out.println("Presione enter para continuar...");
        esperarTecla();
    }

    static void reporte8RankingMejorAsistencia() {
        limpiarPantalla();
        System.out.println("--- RANKING DE MEJOR ASISTENCIA ---");

        double[] promedios = new double[MAX_EST + 1];
        for (int e = 1; e <= cant_estudiantes; e++) {
            double suma = 0;
            int cuenta = 0;
            for (int a = 1; a <= cant_asignaturas; a++) {
                if (asig_clases_realizadas[a] > 0) {
                    suma += calcularPorcentajeAsistencia(a, e);
                    cuenta++;
                }
            }
            promedios[e] = cuenta > 0 ? suma / cuenta : 0;
        }
        
        
      //ordenamiento bubble-- compara izquierda con su +1//
        int[] orden = new int[cant_estudiantes + 1];
        for (int e = 1; e <= cant_estudiantes; e++) orden[e] = e;

        for (int i = 1; i <= cant_estudiantes - 1; i++) {
            for (int j = 1; j <= cant_estudiantes - i; j++) {
                if (promedios[orden[j]] < promedios[orden[j + 1]]) {
                    int temp = orden[j];
                    orden[j] = orden[j + 1];
                    orden[j + 1] = temp; //guarda temp//
                }
            }
        }

        for (int i = 1; i <= cant_estudiantes; i++) {
            int e = orden[i];
            System.out.println(i + ". " + est_nombre[e] + " - " + redondear(promedios[e]) + "%");
        }
        System.out.println("Presione enter para continuar...");
        esperarTecla();
    }

    static void reporte9CantidadFaltasJustificadas() {
        limpiarPantalla();
        System.out.println("--- CANTIDAD DE FALTAS JUSTIFICADAS ---");
        int totalGeneral = 0;
        for (int a = 1; a <= cant_asignaturas; a++) {
            int totalAsig = 0;
            for (int e = 1; e <= cant_estudiantes; e++) {
                totalAsig += justificaciones[a][e];
            }
            System.out.println(asig_nombre[a] + ": " + totalAsig + " falta(s) justificada(s).");
            totalGeneral += totalAsig;
        }
        System.out.println("----------------------------------------");
        System.out.println("Total general: " + totalGeneral + " falta(s) justificada(s).");
        System.out.println("Presione enter para continuar...");
        esperarTecla();
    }

    static void reporte10EstadisticasGenerales() {
        limpiarPantalla();
        System.out.println("--- ESTADISTICAS GENERALES DEL PERIODO ACADEMICO ---");
        System.out.println("Estudiantes matriculados: " + cant_estudiantes);
        System.out.println("Asignaturas activas: " + cant_asignaturas);

        int totalClasesDictadas = 0;
        int totalAsistencias = 0, totalFaltas = 0, totalAtrasos = 0, totalJustificaciones = 0;
        String asigMayorAusentismo = "-";
        double peorPromedioAsistencia = 200;

        for (int a = 1; a <= cant_asignaturas; a++) {
            totalClasesDictadas += asig_clases_realizadas[a];
            double sumaPromedioAsig = 0;
            for (int e = 1; e <= cant_estudiantes; e++) {
                totalAsistencias += asistencias[a][e];
                totalFaltas += faltas[a][e];
                totalAtrasos += atrasos[a][e];
                totalJustificaciones += justificaciones[a][e];
                sumaPromedioAsig += calcularPorcentajeAsistencia(a, e);
            }
            double promedioAsig = cant_estudiantes > 0 ? sumaPromedioAsig / cant_estudiantes : 0;
            if (asig_clases_realizadas[a] > 0 && promedioAsig < peorPromedioAsistencia) {
                peorPromedioAsistencia = promedioAsig;
                asigMayorAusentismo = asig_nombre[a];
            }
        }

        System.out.println("Total de clases dictadas (todas las asignaturas): " + totalClasesDictadas);
        System.out.println("Total de asistencias registradas: " + totalAsistencias);
        System.out.println("Total de faltas: " + totalFaltas);
        System.out.println("Total de atrasos: " + totalAtrasos);
        System.out.println("Total de justificaciones: " + totalJustificaciones);
        System.out.println("Asignatura con mayor ausentismo: " + asigMayorAusentismo +
                (peorPromedioAsistencia <= 100 ? " (" + redondear(peorPromedioAsistencia) + "% de asistencia promedio)" : ""));

        System.out.println("Presione enter para continuar...");
        esperarTecla();
    }

    static void reporte11PromedioPorParalelo() {
        limpiarPantalla();
        System.out.println("--- PROMEDIO DE ASISTENCIA POR PARALELO ---");
        String[] paralelosVistos = new String[MAX_EST + 1];
        int cantParalelos = 0;

        for (int e = 1; e <= cant_estudiantes; e++) {
            String paralelo = est_paralelo[e];
            boolean yaVisto = false;
            for (int i = 1; i <= cantParalelos; i++) if (paralelosVistos[i].equals(paralelo)) yaVisto = true;
            if (!yaVisto) {
                cantParalelos++;
                paralelosVistos[cantParalelos] = paralelo;
            }
        }

        for (int i = 1; i <= cantParalelos; i++) {
            double suma = 0;
            int cuenta = 0;
            for (int e = 1; e <= cant_estudiantes; e++) {
                if (est_paralelo[e].equals(paralelosVistos[i])) {
                    for (int a = 1; a <= cant_asignaturas; a++) {
                        if (asig_clases_realizadas[a] > 0) {
                            suma += calcularPorcentajeAsistencia(a, e);
                            cuenta++;
                        }
                    }
                }
            }
            double promedio = cuenta > 0 ? suma / cuenta : 0;
            System.out.println("Paralelo " + paralelosVistos[i] + ": " + redondear(promedio) + "% de asistencia promedio.");
        }
        System.out.println("Presione enter para continuar...");
        esperarTecla();
    }

   
    static void limpiarPantalla() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        for (int i = 0; i < 3; i++) System.out.println();
    }

    static void esperar(int segundos) {
        try {
            Thread.sleep(segundos * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    static void esperarTecla() {
        sc.nextLine();
    }

    static int leerEntero() {
        while (!sc.hasNextInt()) {
            sc.next();
        }
        int valor = sc.nextInt();
        sc.nextLine();
        return valor;
    }

    static double leerReal() {
        while (!sc.hasNextDouble()) {
            sc.next();
        }
        double valor = sc.nextDouble();
        sc.nextLine();
        return valor;
    }
}
