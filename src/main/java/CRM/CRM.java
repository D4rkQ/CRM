package CRM;

import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
import org.semanticweb.owlapi.vocab.PrefixOWLOntologyFormat;
//import uk.ac.manchester.cs.owlapi.dlsyntax.DLSyntaxObjectRenderer;

import java.io.*;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * CRM OWL Ontology
 * based on some Examples of E. Ammann *
 * by Marcus Danzer & Marcel Sailer @ reutlingen-university
 */

public class CRM {

    private static final String BASE_URL = "http://www.semanticweb.org/max/ontologies/CRM_Ontology";
    private static final String FILE_LOCATION = "CRM_Ontology1.owl";
    private static File output = new File("CRM_Ontology_new.owl");


    static OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
    static File file = new File(FILE_LOCATION);
    static OWLDataFactory factory = manager.getOWLDataFactory();


    public static void main(String[] args) throws OWLOntologyCreationException, IOException, OWLOntologyStorageException {

        //prepare ontology
        OWLOntology ontology = manager.loadOntologyFromOntologyDocument(file);
        PrefixOWLOntologyFormat pm = (PrefixOWLOntologyFormat) manager.getOntologyFormat(ontology);
        pm.setDefaultPrefix(BASE_URL + "#");
        IRI documentIRI = manager.getOntologyDocumentIRI(ontology);
        System.out.println("\nOntology loaded from: " + documentIRI + "\n");

        OWLReasonerFactory reasonerFactory = PelletReasonerFactory.getInstance();


        // Kunde wird erzeugt (Peter)
        OWLIndividual peter = factory.getOWLNamedIndividual(":Peter", pm);
        OWLClass kundenClass = factory.getOWLClass(":Potentielle", pm);
        OWLClassAssertionAxiom caAxiom = factory.getOWLClassAssertionAxiom(kundenClass, peter);
        manager.addAxiom(ontology, caAxiom);

        //Mitarbeiter wird erzeugt (Georg)
        OWLIndividual georg = factory.getOWLNamedIndividual(":Georg", pm);
        OWLClass mitarbeiterClass = factory.getOWLClass(":Potentielle", pm);
        OWLClassAssertionAxiom caAxiom2 = factory.getOWLClassAssertionAxiom(mitarbeiterClass, georg);
        manager.addAxiom(ontology, caAxiom2);

        //Property Assertion Georg betreut Peter
        OWLObjectProperty betreut = factory.getOWLObjectProperty(":betreut", pm);
        OWLObjectPropertyAssertionAxiom pAAxiom1 = factory.getOWLObjectPropertyAssertionAxiom(betreut, georg, peter);
        manager.addAxiom(ontology, pAAxiom1);

        /*
       //Property Assertion Hans betreut Peter (Sollte vom Reasoner bemängelt werden da Objection Restriktion besteht)
       OWLIndividual hans = factory.getOWLNamedIndividual(":Hans", pm);
       OWLObjectPropertyAssertionAxiom pAAxiom2 = factory.getOWLObjectPropertyAssertionAxiom(betreut, georg, hans);
       manager.addAxiom(ontology, pAAxiom2);

        //Die verschiedenen Personen als verschieden deklarieren(Wichtig das sonst Objection Restriktion nicht erkannt wird)
        OWLDifferentIndividualsAxiom dIAaxiom1 = factory.getOWLDifferentIndividualsAxiom(peter, georg, hans);
        manager.addAxiom(ontology, dIAaxiom1);
*/

        //Hans aus besteheneder Ontology auslesen
        OWLIndividual hans = factory.getOWLNamedIndividual(":Hans", pm);



        OWLIndividual warenkorb1 = generateWarenkorb("Warenkorb1231", ontology, pm);
        OWLIndividual historie1 = addWarenkorbToHistorie(warenkorb1, "historie11231", ontology, pm);
        historieZuKundeZuordnen(historie1, peter, ontology, pm);

        OWLIndividual warenkorb2 = generateWarenkorb("warenkorb21313131",ontology,pm);
        addWarenkorbToHistorie(warenkorb2,"HystorieHans",ontology,pm);
        analyseCustomers(ontology, pm);
        validate(reasonerFactory, ontology);
    }

    public static void historieZuKundeZuordnen(OWLIndividual historie, OWLIndividual kunde, OWLOntology ontology, PrefixOWLOntologyFormat pm) {

        OWLObjectProperty kundeIstZugeordnet = factory.getOWLObjectProperty(":kundeIstZugeordnet", pm);
        OWLObjectPropertyAssertionAxiom axiom3 = factory.getOWLObjectPropertyAssertionAxiom(kundeIstZugeordnet, kunde, historie);
        manager.addAxiom(ontology, axiom3);
    }

    public static OWLIndividual addWarenkorbToHistorie(OWLIndividual warenkorb, String hisstorienString, OWLOntology ontology, PrefixOWLOntologyFormat pm) {
        OWLClass einkaufshistorieClass = factory.getOWLClass(":Einkaufshistorie", pm);

        OWLIndividual einkaufshistorie = factory.getOWLNamedIndividual(":" + hisstorienString, pm);
        OWLClassAssertionAxiom axiom1 = factory.getOWLClassAssertionAxiom(einkaufshistorieClass, einkaufshistorie);
        manager.addAxiom(ontology, axiom1);

        OWLObjectProperty historieEnthaelt = factory.getOWLObjectProperty(":historieEnthaelt", pm);
        OWLObjectPropertyAssertionAxiom axiom3 = factory.getOWLObjectPropertyAssertionAxiom(historieEnthaelt, einkaufshistorie, warenkorb);
        manager.addAxiom(ontology, axiom3);

        return einkaufshistorie;

    }

    public static OWLIndividual generateWarenkorb(String warenkorbString, OWLOntology ontology, PrefixOWLOntologyFormat pm) {

        OWLClass warenkorbClass = factory.getOWLClass(":Warenkorb", pm);
        OWLClass produktClass = factory.getOWLClass(":Produkt", pm);
        OWLClass onlineLadenClass = factory.getOWLClass(":OnlineLaden", pm);

        OWLIndividual grueneSchuhe = factory.getOWLNamedIndividual(":GrueneSchuhe", pm);
        OWLIndividual warenkorb = factory.getOWLNamedIndividual(":" + warenkorbString, pm);
        OWLIndividual onlineLaden1 = factory.getOWLNamedIndividual(":SchuhLaden", pm);


        OWLClassAssertionAxiom axiom1 = factory.getOWLClassAssertionAxiom(produktClass, grueneSchuhe);
        manager.addAxiom(ontology, axiom1);

        OWLClassAssertionAxiom axiom2 = factory.getOWLClassAssertionAxiom(warenkorbClass, warenkorb);
        manager.addAxiom(ontology, axiom2);

        OWLClassAssertionAxiom axiom5 = factory.getOWLClassAssertionAxiom(onlineLadenClass, onlineLaden1);
        manager.addAxiom(ontology, axiom5);

        OWLObjectProperty enthaelt = factory.getOWLObjectProperty(":enthaelt", pm);
        OWLObjectPropertyAssertionAxiom axiom3 = factory.getOWLObjectPropertyAssertionAxiom(enthaelt, warenkorb, grueneSchuhe);
        manager.addAxiom(ontology, axiom3);

        OWLObjectPropertyAssertionAxiom axiom4 = factory.getOWLObjectPropertyAssertionAxiom(enthaelt, warenkorb, onlineLaden1);
        manager.addAxiom(ontology, axiom4);
        return warenkorb;

    }

    public static void analyseCustomers(OWLOntology ontology, PrefixOWLOntologyFormat pm) {
        OWLClass kundenClass = factory.getOWLClass(":Kunde", pm);
        OWLClass potentielleClass = factory.getOWLClass(":Potentielle", pm);
        OWLClass vipClass = factory.getOWLClass(":VIP", pm);
        OWLClass standardClass = factory.getOWLClass(":Standard", pm);
        Set<OWLClassAssertionAxiom> classAssertionAxiomSet1 = ontology.getClassAssertionAxioms(kundenClass);
        Set<OWLClassAssertionAxiom> classAssertionAxiomSet2 = ontology.getClassAssertionAxioms(potentielleClass);
        Set<OWLClassAssertionAxiom> classAssertionAxiomSet3 = ontology.getClassAssertionAxioms(vipClass);
        Set<OWLClassAssertionAxiom> classAssertionAxiomSet4 = ontology.getClassAssertionAxioms(standardClass);

        classAssertionAxiomSet1.addAll(classAssertionAxiomSet2);
        classAssertionAxiomSet1.addAll(classAssertionAxiomSet3);
        classAssertionAxiomSet1.addAll(classAssertionAxiomSet4);

        for (OWLClassAssertionAxiom x : classAssertionAxiomSet1) {

            OWLIndividual individual = x.getIndividual();
            OWLClass indClass = x.getClassesInSignature().iterator().next();
            int count;
            try {

                count = getCountForIndividual(individual, pm, ontology);
            } catch (Exception e) {
                count = 0;
            }

            manager.removeAxiom(ontology, x);

            if (count > 2) {

                OWLClassAssertionAxiom caAxiom = factory.getOWLClassAssertionAxiom(vipClass, individual);
                manager.addAxiom(ontology, caAxiom);

                Map<OWLDataPropertyExpression, Set<OWLLiteral>> dataPropertysMap = individual.getDataPropertyValues(ontology);
                OWLDataProperty dataPropertyRabatt = factory.getOWLDataProperty(":Rabatt", pm);

                OWLLiteral owlLiteral = factory.getOWLLiteral(":15%");

                OWLDataPropertyAssertionAxiom owlDataPropertyAssertionAxiom = factory.getOWLDataPropertyAssertionAxiom(dataPropertyRabatt, individual, owlLiteral);
                manager.addAxiom(ontology, owlDataPropertyAssertionAxiom);

            } else if (count > 0) {
                OWLClassAssertionAxiom caAxiom = factory.getOWLClassAssertionAxiom(standardClass, individual);
                manager.addAxiom(ontology, caAxiom);

                Map<OWLDataPropertyExpression, Set<OWLLiteral>> dataPropertysMap = individual.getDataPropertyValues(ontology);
                OWLDataProperty dataPropertyRabatt = factory.getOWLDataProperty(":Rabatt", pm);

                OWLLiteral owlLiteral = factory.getOWLLiteral(":10%");

                OWLDataPropertyAssertionAxiom owlDataPropertyAssertionAxiom = factory.getOWLDataPropertyAssertionAxiom(dataPropertyRabatt, individual, owlLiteral);
                manager.addAxiom(ontology, owlDataPropertyAssertionAxiom);

            } else {
                OWLClassAssertionAxiom caAxiom = factory.getOWLClassAssertionAxiom(potentielleClass, individual);
                manager.addAxiom(ontology, caAxiom);

                Map<OWLDataPropertyExpression, Set<OWLLiteral>> dataPropertysMap = individual.getDataPropertyValues(ontology);
                OWLDataProperty dataPropertyRabatt = factory.getOWLDataProperty(":Rabatt", pm);

                OWLLiteral owlLiteral = factory.getOWLLiteral(":5%");

                OWLDataPropertyAssertionAxiom owlDataPropertyAssertionAxiom = factory.getOWLDataPropertyAssertionAxiom(dataPropertyRabatt, individual, owlLiteral);
                manager.addAxiom(ontology, owlDataPropertyAssertionAxiom);

            }
        }

    }

    public static int getCountForIndividual(OWLIndividual individual, PrefixOWLOntologyFormat pm, OWLOntology ontology) {
        Map<OWLObjectPropertyExpression, Set<OWLIndividual>> hansObjectPropertyValues = individual
                .getObjectPropertyValues(ontology);
        OWLObjectProperty kundeIstZugeordnet = factory.getOWLObjectProperty(":kundeIstZugeordnet", pm);
        OWLClass historieClass = factory.getOWLClass(":Einkaufshistorie", pm);
        Set<OWLIndividual> owlIndividualSetHisorie = hansObjectPropertyValues.get(kundeIstZugeordnet);
        Iterator<OWLIndividual> iterator = owlIndividualSetHisorie.iterator();
        OWLIndividual historie = iterator.next();
        OWLClassAssertionAxiom caAxiom3 = factory.getOWLClassAssertionAxiom(historieClass, historie);

        return getCount(":historieEnthaelt", ":Warenkorb", ontology, historie, pm);

    }

    public static void validate(OWLReasonerFactory reasonerFactory, OWLOntology ontology) throws OWLOntologyStorageException {
        //Valide Ontology speichern
        OWLReasoner reasoner = reasonerFactory.createReasoner(ontology, new SimpleConfiguration());
        if (reasoner.isConsistent()) {
            IRI documentIRI2 = IRI.create(output);
            manager.saveOntology(ontology, documentIRI2);
            System.out.println("\nExtended Ontology stored in " + documentIRI2 + "\n");
        } else {
            System.out.println("The created Ontology is not valid!! Please check constraints");
        }
    }

    public static int getCount(String propertyString, String classString, OWLOntology ontology,
                               OWLIndividual historie, PrefixOWLOntologyFormat pm) {
        int count = 0;
        Map<OWLObjectPropertyExpression, Set<OWLIndividual>> objectPropertyValues = historie.getObjectPropertyValues(ontology);
        OWLObjectProperty property = factory.getOWLObjectProperty(propertyString, pm);
        Set<OWLIndividual> owlIndividualSetWarenkorb = objectPropertyValues.get(property);

        for (OWLIndividual x : owlIndividualSetWarenkorb) {
            count++;
        }

        return count;

    }
}