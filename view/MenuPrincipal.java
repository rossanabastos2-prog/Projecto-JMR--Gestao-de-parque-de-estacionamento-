
package view;

import controller.EstacionamentoController;
import java.util.Scanner;

public class MenuPrincipal {

    public static void main(String[] args) {

        EstacionamentoController controller = new EstacionamentoController();
        Dashboard dashboard = new Dashboard();

        try (Scanner scanner = new Scanner(System.in)) {
            int opcao = -1;

            while (opcao != 6) {

                exibirMenu();

                try {
                    opcao = scanner.nextInt();
                    scanner.nextLine();

                } catch (Exception e) {
                    System.out.println("\n  ⚠ Por favor, introduza um número válido.");
                    scanner.nextLine();
                    continue;
                }

                switch (opcao) {
                    case 1 -> {
                        System.out.print("\n  Digite a matrícula do veículo : ");
                        String matricula = scanner.nextLine().trim().toUpperCase();

                        System.out.print("  Digite o tipo (Carro / Mota)  : ");
                        String tipo = scanner.nextLine().trim();

                        System.out.println();
                        System.out.println("  " +
                                controller.registarEntrada(matricula, tipo));

                        pausar(scanner);
                    }
                    case 2 -> {
                        System.out.print("\n  Digite a matrícula para saída : ");
                        String matSaida = scanner.nextLine().trim().toUpperCase();

                        System.out.println();
                        System.out.println(controller.registarSaida(matSaida));

                        pausar(scanner);
                    }
                    case 3 -> {
                        dashboard.exibirDashboard(
                                controller.getVagas(),
                                controller.getSessaoStats(),
                                controller.getVeiculoMaisAntigo(),
                                controller.getTempoMedioPermanencia()
                        );

                        pausar(scanner);
                    }
                    case 4 -> {
                        controller.exibirEstadoParque();

                        pausar(scanner);
                    }
                    case 5 -> {
                        System.out.println("\n  A exportar relatório da sessão...");

                        String ficheiro = controller.exportarRelatorio();

                        if (ficheiro != null) {
                            System.out.println("  Relatório exportado com sucesso.");
                            System.out.println("  Ficheiro: " + ficheiro);
                        } else {
                            System.out.println("  Erro ao exportar relatório.");
                        }

                        pausar(scanner);
                    }
                    case 6 -> {
                        System.out.println("\n  A guardar dados do parque...");

                        boolean guardado = controller.guardarDados();

                        if (guardado) {
                            System.out.println("  Dados guardados com sucesso.");
                        } else {
                            System.out.println("  AVISO: Não foi possível guardar os dados.");
                        }

                        System.out.println("\n  A encerrar o sistema JMR... Até à próxima!\n");
                    }
                    default -> System.out.println("\n  ⚠ Opção inválida! Escolha entre 1 e 6.");
                }
 
                            }
        }
    }



    private static void exibirMenu() {

        System.out.println();
        System.out.println("  ╔══════════════════════════════════════╗");
        System.out.println("  ║   JMR – Gestão de Estacionamento     ║");
        System.out.println("  ╠══════════════════════════════════════╣");
        System.out.println("  ║  1. Registar Entrada de Veículo      ║");
        System.out.println("  ║  2. Registar Saída (Pagamento)       ║");
        System.out.println("  ║  3. Dashboard Visual do Parque       ║");
        System.out.println("  ║  4. Consultar Estado (Lista)         ║");
        System.out.println("  ║  5. Exportar Relatório               ║");
        System.out.println("  ║  6. Guardar Dados e Sair             ║");
        System.out.println("  ╚══════════════════════════════════════╝");

        System.out.print("  Escolha uma opção: ");
    }


    private static void pausar(Scanner scanner) {

        System.out.println();
        System.out.print("  [ Pressione ENTER para continuar... ]");

        scanner.nextLine();
    }
}

