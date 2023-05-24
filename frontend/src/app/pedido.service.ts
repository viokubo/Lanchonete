import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class PedidoService {
  private apiUrl = 'http://localhost:8080';

  constructor(private http: HttpClient) {}

  criarPedido() {
    return this.http.post<number>(`${this.apiUrl}/pedidos`, {});
  }

  getProdutos() {
    return this.http.get<any[]>(`${this.apiUrl}/produtos`);
  }

  adicionarProduto(pedidoId: number, produtoId: number, quantidade: number) {
    return this.http.post(
      `${this.apiUrl}/pedidos/${pedidoId}/adicionar-produto?produtoId=${produtoId}&quantidade=${quantidade}`,
      {}
    );
  }

  calcularPrecoTotal(pedidoId: number) {
    return this.http.get<number>(
      `${this.apiUrl}/pedidos/${pedidoId}/calcular-preco-total`
    );
  }

  fecharPedido(pedidoId: number, valorPagamento: number) {
    return this.http.post<string>(
      `${this.apiUrl}/pedidos/${pedidoId}/fechar-pedido?valorPagamento=${valorPagamento}`,
      {}
    );
  }
}
