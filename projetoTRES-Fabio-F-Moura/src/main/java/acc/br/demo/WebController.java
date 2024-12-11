package acc.br.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class WebController {
    @Autowired
    private ScoreRepository scoreRepo;

    @ResponseBody
    @GetMapping("/score")
    public Score getScore() {
        Score score;
        try {
            score = scoreRepo.findById(1).get();
        } catch (Exception e) {
            score = new Score(0, 0, 0);
            scoreRepo.save(score);
        }
        return score;
    }

    @GetMapping("/teste")
    public String resultado(@RequestParam(name = "escolha", required = false) String aEscolha, Model model) {
        String saida = "Empate";
        
        if (aEscolha != null) {
            if (aEscolha.equalsIgnoreCase("Papel")) {
                saida = "Voce ganhou!! :) ";
                incrementScore(true, false, false); 
            } else if (aEscolha.equalsIgnoreCase("Tesoura")) {
                saida = "Você perdeu! :(";
                incrementScore(false, true, false);
            } else if (aEscolha.equalsIgnoreCase("Pedra")) {
                saida = "Empate!";
                incrementScore(false, false, true);
            }
            model.addAttribute("aEscolha", aEscolha);
        }

        Score score = scoreRepo.findById(1).orElse(new Score(0, 0, 0));
        model.addAttribute("saida", saida);
        model.addAttribute("score", score);       
        model.addAttribute("zerarScoreUrl", "/zerarScore");
        model.addAttribute("tentarNovamenteUrl", "/tentarNovamente");

        return "resultado";
    }

    @GetMapping("/zerarScore")
    public String zerarScore(Model model) {
        Score score = scoreRepo.findById(1).orElse(new Score(0, 0, 0));
        score.setVitorias(0);
        score.setDerrotas(0);
        score.setEmpates(0);
        scoreRepo.save(score);
        
        return "redirect:/index.html";
    }   
      
    private void incrementScore(boolean venceu, boolean perdeu, boolean empate) {
        Score score = scoreRepo.findById(1).orElse(new Score(0, 0, 0));
        if (venceu) {
            score.setVitorias(score.getVitorias() + 1);
        }
        if (perdeu) {
            score.setDerrotas(score.getDerrotas() + 1);
        }
        if (empate) {
            score.setEmpates(score.getEmpates() + 1);
        }
        scoreRepo.save(score);
    }
        
}
