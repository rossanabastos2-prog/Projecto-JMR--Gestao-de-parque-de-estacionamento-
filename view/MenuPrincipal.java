package view;

import controller.EstacionamentoController;
import java.util.Scanner;

public class MenuPrincipal {
    public static void main(String[] args) {
        // Instancia o controlador (Controller) que gerencia as ações
        EstacionamentoController controller = new EstacionamentoController();
        try (Scanner scanner = new Scanner(System.in)) {
            int opcao = 0;
            
            do {
                System.out.println("\n=== SISTEMA DE GESTÃO DE ESTACIONAMENTO ===");
                System.out.println("1. Registar Entrada de Veículo");
                System.out.println("2. Registar Saída (Pagamento)");
                System.out.println("3. Consultar Estado do Parque");
                System.out.println("4. Sair do Sistema");
                System.out.print("Escolha uma opção: ");
                
                try {
                    opcao = scanner.nextInt();
                    scanner.nextLine(); // Limpar buffer do teclado
                } catch (Exception e) {
                    System.out.println("Por favor, digite um número válido.");
                    scanner.nextLine();
                    continue;
                }
                
                switch (opcao) {
                    case 1 -> {
                        System.out.print("Digite a matrícula do veículo: ");
                        String matricula = scanner.nextLine();
                        System.out.print("Digite o tipo (Carro/Mota): ");
                        String tipo = scanner.nextLine();
                        
                        String resultadoEntrada = controller.registarEntrada(matricula, tipo);
                        System.out.println(resultadoEntrada);
                    }
                        
                    case 2 -> {
                        System.out.print("Digite a matrícula para a saída: ");
                        String matSaida = scanner.nextLine();
                        
                        String resultadoSaida = controller.registarSaida(matSaida);
                        System.out.println(resultadoSaida);
                    }
                        
                    case 3 -> controller.exibirEstadoParque();
                        
                    case 4 -> System.out.println("A encerrar o sistema... Até à próxima!");
                        
                    default -> System.out.println("Opção inválida! Tente novamente.");
                }
            } while (opcao != 4);
        }
    }
}